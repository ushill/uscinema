package com.ushill.controller.v1.movie;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.DTO.MovieSummaryDTO;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.service.interfaces.MovieService;
import com.ushill.utils.stat.CachedStatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/v1/movies/summary")
@Validated
public class MovieSummaryListController {

    private final MovieService movieService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;

    @Value("${uscinema.cached-key.v1.nowplaying}")
    private String nowPlayingCachedKey;
    @Value("${uscinema.cached-key.v1.coming-soon}")
    private String comingSoonCachedKey;

    @Autowired
    public MovieSummaryListController(@Qualifier("movieServiceImpl") MovieService movieService,
                                      StringRedisTemplate redisTemplate,
                                      ObjectMapper objectMapper,
                                      CachedStatMap cachedStatMap1) {
        this.movieService = movieService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap1;
    }

    @GetMapping("/nowplaying")
    public List<MovieSummaryDTO> nowPlayingSummary() throws IOException {
        String nowplayingCache = redisTemplate.opsForValue().get(nowPlayingCachedKey);

        if(nowplayingCache != null){
            cachedStatMap.hitIncrement(nowPlayingCachedKey);
            return objectMapper.readValue(nowplayingCache, new TypeReference<List<MovieSummaryDTO>>(){});
        }

        List<MovieSummaryDTO> list = movieService.getNowPlayingMoviesSummary();
        redisTemplate.opsForValue().set(nowPlayingCachedKey, objectMapper.writeValueAsString(list), 4, TimeUnit.HOURS);
        cachedStatMap.missIncrement(nowPlayingCachedKey);

        return list;
    }

    @GetMapping("/coming_soon")
    public List<MovieSummaryDTO> comingSoonSummary() throws IOException {
        String comingSoonCache = redisTemplate.opsForValue().get(comingSoonCachedKey);

        if(comingSoonCache != null){
            cachedStatMap.hitIncrement(comingSoonCachedKey);
            return objectMapper.readValue(comingSoonCache, new TypeReference<List<MovieSummaryDTO>>(){});
        }

        List<MovieSummaryDTO> list = movieService.getComingSoonMoviesSummary();
        redisTemplate.opsForValue().set(comingSoonCachedKey, objectMapper.writeValueAsString(list), 4, TimeUnit.HOURS);
        cachedStatMap.missIncrement(comingSoonCachedKey);
        return list;
    }


    @GetMapping("/test")
    @ScopeLevel(8)
    public List<MovieSummaryDTO> test() throws IOException {
        Date date1 = new Date();

        List<MovieSummaryDTO> list = null;

        for(int i = 0; i<100000; i++) {
            list = movieService.getNowPlayingMoviesSummary();
        }
        Date date2 = new Date();
        System.out.println("不用缓存，查询100000次，耗时：：" + (date2.getTime()-date1.getTime())/1000 + "秒");

        date1 = new Date();

        for(int i = 0; i<100000; i++) {
            String nowplayingCache = (String) redisTemplate.opsForValue().get(nowPlayingCachedKey);

            if(nowplayingCache != null){
                cachedStatMap.hitIncrement(nowPlayingCachedKey);
                list = objectMapper.readValue(nowplayingCache, new TypeReference<List<MovieSummaryDTO>>(){});
                continue;
            }

            list = movieService.getNowPlayingMoviesSummary();
            redisTemplate.opsForValue().set(nowPlayingCachedKey, objectMapper.writeValueAsString(list), 4, TimeUnit.HOURS);
            cachedStatMap.missIncrement(nowPlayingCachedKey);
        }
        date2 = new Date();
        System.out.println("使用缓存，查询100000次，耗时：" + (date2.getTime()-date1.getTime())/1000 + "秒");
        System.out.println(date2.getTime()-date1.getTime());

        return list;
    }

}

