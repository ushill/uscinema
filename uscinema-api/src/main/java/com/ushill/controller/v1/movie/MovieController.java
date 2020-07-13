package com.ushill.controller.v1.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.VO.movie.MovieDetailsVO;
import com.ushill.models.Movie;
import com.ushill.service.interfaces.MovieService;
import com.ushill.utils.stat.CachedStatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/movie")
public class MovieController {

    private final MovieService movieService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;

    @Value("${uscinema.cached-key.v1.movie.detail}")
    private String movieCachedKey;

    @Autowired
    MovieController(@Qualifier("movieServiceImpl") MovieService movieService,
                    StringRedisTemplate redisTemplate, ObjectMapper objectMapper, CachedStatMap cachedStatMap){
        this.movieService = movieService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap;
    }

    @GetMapping("/detail/{movieId}")
    public MovieDetailsVO movieDetails(@PathVariable @Min(value = 10000000, message = "电影ID不合法")
                          @Max(value = 200000000, message = "电影ID不合法") String movieId) throws IOException {

        String topCache = redisTemplate.opsForValue().get(movieCachedKey + ":" + movieId);
        if(topCache != null){
            cachedStatMap.hitIncrement(movieCachedKey);
            return objectMapper.readValue(topCache, MovieDetailsVO.class);
        }

        Movie movie = movieService.getMovie(Integer.parseInt(movieId));
        MovieDetailsVO movieDetailsVO = new MovieDetailsVO(movie);

        redisTemplate.opsForValue().set(movieCachedKey + ":" + movieId,
                objectMapper.writeValueAsString(movieDetailsVO), 1, TimeUnit.HOURS);
        cachedStatMap.missIncrement(movieCachedKey);
        return movieDetailsVO;
    }

}
