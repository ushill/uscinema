package com.ushill.controller.v1.comments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.DTO.CommentSummaryDTO;
import com.ushill.service.interfaces.comments.MovieCommentsService;
import com.ushill.utils.BoolUtils;
import com.ushill.utils.stat.CachedStatMap;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @description：评论模块API -- 评论列表类数据
 * @author     ：五羊
 * @date       ：2020/3/26 下午10:48
 */
@RestController
@RequestMapping("/v1/comments")
@Validated
public class MovieCommentListController {

    private final MovieCommentsService commentService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;

    @Value("${uscinema.cached-key.v1.movie.comments}")
    private String commentsCachedKey;

    @Autowired
    public MovieCommentListController(@Qualifier("movieCommentsServiceImpl") MovieCommentsService commentService,
                                      StringRedisTemplate redisTemplate, ObjectMapper objectMapper, CachedStatMap cachedStatMap) {
        this.commentService = commentService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap;
    }

    /**
     * @description: 获取某部电影的首页展示评论
     * @author: 五羊
     * @date: 2020/3/26 下午10:49
     * @params: String movieId: 电影ID
     *          String isCritic: 1 作者 / 0 用户
     * @return
     */
    @GetMapping("/summary/movie/{movieId}")
    public List<CommentSummaryDTO> movieCommentsSummary(
            @PathVariable @Min(value = 10000000, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId,
            @RequestParam(defaultValue = "1") String isCritic){

        return commentService.getMovieCommentsSummary(Integer.parseInt(movieId),
                BoolUtils.trueOrFalse(isCritic, 30001));
    }

    /**
     * @description: 获取某部电影的某页评论列表
     * @author: 五羊
     * @date: 2020/3/26 下午10:49
     * @params: String movieId: 电影ID
     *          String isCritic: 1 作者 / 0 用户
     *          String page: 页码
     * @return
     */
    @GetMapping("/movie/{movieId}")
    public List<CommentSummaryDTO> movieComments(
            @PathVariable @Min(value = 10000000, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId,
            @RequestParam(defaultValue = "1") String isCritic,
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 50, message = "页数不能超过50") String page) throws IOException {

        String cacheKey = commentsCachedKey + ":" + movieId + ":" + isCritic + ":" + page;
        String commentsCache = redisTemplate.opsForValue().get(cacheKey);
        if(commentsCache != null){
            cachedStatMap.hitIncrement(commentsCachedKey);
            return objectMapper.readValue(commentsCache,  new TypeReference<List<CommentSummaryDTO>>(){});
        }

        List<CommentSummaryDTO> movieComments = commentService.getMovieComments(Integer.parseInt(movieId),
                BoolUtils.trueOrFalse(isCritic, 30001), Integer.parseInt(page));

        redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(movieComments), 1, TimeUnit.HOURS);
        cachedStatMap.missIncrement(commentsCachedKey);
        return movieComments;
    }
}
