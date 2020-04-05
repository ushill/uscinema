package com.ushill.controller.v1.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.VO.movie.MoviesListVO;
import com.ushill.models.Movie;
import com.ushill.service.interfaces.MovieService;
import com.ushill.utils.BoolUtils;
import com.ushill.utils.PagedGridResult;
import com.ushill.utils.stat.CachedStatMap;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/v1/movies")
@Validated
public class MovieListController {

    private final MovieService movieService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;

    @Value("${uscinema.cached-key.v1.movie-list.all}")
    private String allCachedKey;
    @Value("${uscinema.cached-key.v1.movie-list.coming-soon}")
    private String comingSoonCachedKey;
    @Value("${uscinema.cached-key.v1.movie-list.bnm}")
    private String bnmCachedKey;
    @Value("${uscinema.cached-key.v1.movie-list.top}")
    private String topCachedKey;

    @Autowired
    public MovieListController(@Qualifier("movieServiceImpl") MovieService movieService,
                               StringRedisTemplate redisTemplate, ObjectMapper objectMapper, CachedStatMap cachedStatMap) {
        this.movieService = movieService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap;
    }

    @GetMapping("/all")
    public MoviesListVO allMovies(
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 300) String page,
            @RequestParam(defaultValue = "true") String scoped) throws IOException {

        String allCache = redisTemplate.opsForValue().get(allCachedKey + ":" + scoped + ":" + page);
        if(allCache != null){
            cachedStatMap.hitIncrement(allCachedKey);
            return objectMapper.readValue(allCache, MoviesListVO.class);
        }

        PagedGridResult<Movie> grid = movieService.getAllMovies(Integer.parseInt(page), BoolUtils.trueOrFalse(scoped));
        MoviesListVO moviesListVO = new MoviesListVO(grid);

        redisTemplate.opsForValue().set(allCachedKey + ":" + scoped + ":" + page,
                objectMapper.writeValueAsString(moviesListVO), Integer.parseInt(page) <= 10? 2: 10, TimeUnit.HOURS);
        cachedStatMap.missIncrement(allCachedKey);
        return moviesListVO;
    }

    @GetMapping("/coming_soon")
    public MoviesListVO comingSoonMovies(
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 10) String page) throws IOException {

        String comingCache = redisTemplate.opsForValue().get(comingSoonCachedKey + ":" + page);
        if(comingCache != null){
            cachedStatMap.hitIncrement(comingSoonCachedKey);
            return objectMapper.readValue(comingCache, MoviesListVO.class);
        }

        PagedGridResult<Movie> grid = movieService.getComingSoonMovies(Integer.parseInt(page));
        MoviesListVO moviesListVO = new MoviesListVO(grid);

        redisTemplate.opsForValue().set(comingSoonCachedKey + ":" + page,
                objectMapper.writeValueAsString(moviesListVO), 2, TimeUnit.HOURS);
        cachedStatMap.missIncrement(comingSoonCachedKey);
        return moviesListVO;
    }

    @GetMapping("/bnm")
    public MoviesListVO bestNewMovies(
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 30) String page) throws IOException {

        String bnmCache = redisTemplate.opsForValue().get(bnmCachedKey + ":" + page);
        if(bnmCache != null){
            cachedStatMap.hitIncrement(bnmCachedKey);
            return objectMapper.readValue(bnmCache, MoviesListVO.class);
        }

        PagedGridResult<Movie> grid = movieService.getBestNewMovies(Integer.parseInt(page));
        MoviesListVO moviesListVO = new MoviesListVO(grid);

        redisTemplate.opsForValue().set(bnmCachedKey + ":" + page,
                objectMapper.writeValueAsString(moviesListVO), 2, TimeUnit.HOURS);
        cachedStatMap.missIncrement(bnmCachedKey);
        return moviesListVO;
    }

    @GetMapping("/top")
    public MoviesListVO topMovies(
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 30) String page,
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 2) String type) throws IOException {

        String topCache = redisTemplate.opsForValue().get(topCachedKey + ":" + type + ":" + page);
        if(topCache != null){
            cachedStatMap.hitIncrement(topCachedKey);
            return objectMapper.readValue(topCache, MoviesListVO.class);
        }

        PagedGridResult<Movie> grid = movieService.getTopMovies(Integer.parseInt(page), Integer.parseInt(type));
        MoviesListVO moviesListVO = new MoviesListVO(grid);

        redisTemplate.opsForValue().set(topCachedKey + ":" + type + ":" + page,
                objectMapper.writeValueAsString(moviesListVO), 2, TimeUnit.HOURS);
        cachedStatMap.missIncrement(topCachedKey);
        return moviesListVO;
    }
}

