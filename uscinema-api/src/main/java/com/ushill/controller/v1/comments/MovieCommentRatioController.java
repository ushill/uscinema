package com.ushill.controller.v1.comments;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.DTO.CommentRatioDTO;
import com.ushill.service.interfaces.comments.MovieCommentsService;
import com.ushill.utils.BoolUtils;
import com.ushill.utils.stat.CachedStatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @description：评论模块API -- 评分比例类数据
 * @author     ：五羊
 * @date       ：2020/3/26 下午10:48
 */
@RestController
@RequestMapping("/v1/comments")
@Validated
public class MovieCommentRatioController {

    private MovieCommentsService commentService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;

    @Value("${uscinema.cached-key.v1.movie.comment-ratio}")
    private String commentsRatioCachedKey;

    @Autowired
    public MovieCommentRatioController(@Qualifier("movieCommentsServiceImpl") MovieCommentsService commentService,
                                       StringRedisTemplate redisTemplate, ObjectMapper objectMapper, CachedStatMap cachedStatMap) {
        this.commentService = commentService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap;
    }

    @GetMapping("/ratio/movie/{movieId}")
    public CommentRatioDTO movieCommentsRatio(
            @PathVariable @Min(value = 10000000, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId,
            @RequestParam(defaultValue = "1") String isCritic) throws IOException {

        String cacheKey = commentsRatioCachedKey + ":" + movieId + ":" + isCritic;
        String ratioCache = redisTemplate.opsForValue().get(cacheKey);
        if(ratioCache != null){
            cachedStatMap.hitIncrement(commentsRatioCachedKey);
            return objectMapper.readValue(ratioCache,  CommentRatioDTO.class);
        }

        CommentRatioDTO ratio = commentService.getMovieCommentsRatio(Integer.parseInt(movieId),
                BoolUtils.trueOrFalse(isCritic, 30001));

        redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(ratio), 1, TimeUnit.HOURS);
        cachedStatMap.missIncrement(commentsRatioCachedKey);
        return ratio;
    }
}
