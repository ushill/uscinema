package com.ushill.controller.v1.comments;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushill.DTO.*;
import com.ushill.VO.userComments.UserCommentsSummaryVO;
import com.ushill.VO.userComments.UserCommentsVO;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.service.interfaces.comments.UserCommentsService;
import com.ushill.utils.PagedGridResult;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/comments")
@Validated
public class UserCommentsController {

    private final UserCommentsService commentService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final CachedStatMap cachedStatMap;

    @Value("${uscinema.cached-key.v1.critics}")
    private String criticsCachedKey;
    @Value("${uscinema.cached-key.v1.user.cmts}")
    private String commentsCachedKey;

    @Autowired
    public UserCommentsController(@Qualifier("userCommentsServiceImpl") UserCommentsService commentService,
                                  StringRedisTemplate redisTemplate, ObjectMapper objectMapper, CachedStatMap cachedStatMap) {
        this.commentService = commentService;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.cachedStatMap = cachedStatMap;
    }

    @GetMapping("/user/{userId}")
    public Object userComments(
            @PathVariable @Min(value = 1, message = "用户ID不合法")
            @Max(value = 200000, message = "用户ID不合法") String userId,
            @RequestParam(value = "req_type") @Range(min = 2, max = 3, message = "不支持的请求类型") String reqType,
            @RequestParam(defaultValue = "1") @Range(min = 1, max = 300, message = "页数不能超过300") String page) throws IOException {

        String cacheKey = commentsCachedKey + ":" + userId + ":" + reqType + ":" + page;
        String commentsCache = redisTemplate.opsForValue().get(cacheKey);
        if(commentsCache != null){
            cachedStatMap.hitIncrement(commentsCachedKey);
            if(2 == Integer.parseInt(reqType)){
                Object o = objectMapper.readValue(commentsCache,  UserCommentsSummaryVO.class);
                return o;
            }else{
                return objectMapper.readValue(commentsCache,  UserCommentsVO.class);
            }
        }

        Object ret;
        if (2 == Integer.parseInt(reqType)) {
            PagedGridResult<UserCommentSummaryDTO> userCommentsSummary
                    = commentService.getUserCommentsSummary(Integer.parseInt(userId), Integer.parseInt(page));
            ret = new UserCommentsSummaryVO(userCommentsSummary);
        } else {
            PagedGridResult<UserCommentDTO> userComments
                    = commentService.getUserComments(Integer.parseInt(userId), Integer.parseInt(page));
            ret = new UserCommentsVO(userComments);
        }

        redisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(ret),
                Integer.parseInt(page) > 5? 1: 30, TimeUnit.MINUTES);
        cachedStatMap.missIncrement(commentsCachedKey);

        return ret;
    }

    @GetMapping("/critics")
    public List<UserStatDTO> criticsStats(@RequestParam(value = "req_type")
                                   @Range(min = 1, max = 2, message = "不支持的请求类型") String reqType) throws IOException {

        String commentsCache = redisTemplate.opsForValue().get(criticsCachedKey + ":" + reqType);
        if(commentsCache != null){
            cachedStatMap.hitIncrement(criticsCachedKey);
            return objectMapper.readValue(commentsCache,  new TypeReference<List<UserStatDTO>>(){});
        }

        List<UserStatDTO> criticsStat = commentService.getCriticsStat(Integer.parseInt(reqType));

        redisTemplate.opsForValue().set(criticsCachedKey + ":" + reqType,
                objectMapper.writeValueAsString(criticsStat), 20, TimeUnit.HOURS);
        cachedStatMap.missIncrement(criticsCachedKey);
        return criticsStat;
    }

    @GetMapping("/test")
    @ScopeLevel(8)
    public List<UserStatDTO> test(@RequestParam(value = "req_type")
                                       @Range(min = 1, max = 2, message = "不支持的请求类型") String reqType) throws IOException {
        Date date1 = new Date();

        List<UserStatDTO> list = null;

        for(int i = 0; i<1000; i++) {
            list = commentService.getCriticsStat(Integer.parseInt(reqType));
        }
        Date date2 = new Date();
        System.out.println("不用缓存，查询1000次，耗时：" + (double)(date2.getTime()-date1.getTime())/1000 + "秒");

        date1 = new Date();

        for(int i = 0; i<1000; i++) {
            String commentsCache = redisTemplate.opsForValue().get(criticsCachedKey + ":" + reqType);
            if(commentsCache != null){
                cachedStatMap.hitIncrement(criticsCachedKey);
                list = objectMapper.readValue(commentsCache,  new TypeReference<List<UserStatDTO>>(){});
                continue;
            }

            list = commentService.getCriticsStat(Integer.parseInt(reqType));

            redisTemplate.opsForValue().set(criticsCachedKey + ":" + reqType,
                    objectMapper.writeValueAsString(list), 20, TimeUnit.HOURS);
            cachedStatMap.missIncrement(criticsCachedKey);
        }
        date2 = new Date();
        System.out.println("使用缓存，查询1000次，耗时：：" + (double)(date2.getTime()-date1.getTime())/1000 + "秒");

        return list;
    }
}
