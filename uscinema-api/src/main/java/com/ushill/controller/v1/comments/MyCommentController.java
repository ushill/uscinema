package com.ushill.controller.v1.comments;

import com.ushill.VO.userComments.MyCommentVO;
import com.ushill.exception.http.ParameterException;
import com.ushill.interceptor.LocalUser;
import com.ushill.interceptor.ScopeLevel;
import com.ushill.models.UserComments;
import com.ushill.msg.producer.RabbitProducer;
import com.ushill.service.interfaces.comments.MyCommentsService;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/comment/my")
@Validated
public class MyCommentController {

    private final MyCommentsService commentService;
    private final StringRedisTemplate redisTemplate;
    private final RabbitProducer movieUpdateMsgRabbitProducer;

    @Value("${uscinema.cached-key.v1.user-stat}")
    private String userStatKey;

    @Value("${uscinema.cached-key.v1.user.cmts}")
    private String commentsCachedKey;

    @Value("${uscinema.message.update-rating.ex}")
    private String exchange;

    @Value("${uscinema.message.update-rating.rk}")
    private String routingKey;

    @Autowired
    public MyCommentController(@Qualifier("myCommentsServiceImpl") MyCommentsService commentService,
                               StringRedisTemplate redisTemplate, RabbitProducer movieUpdateMsgRabbitProducer) {
        this.commentService = commentService;
        this.redisTemplate = redisTemplate;
        this.movieUpdateMsgRabbitProducer = movieUpdateMsgRabbitProducer;
    }

    @ScopeLevel
    @GetMapping("/movie/{movieId}")
    public MyCommentVO getMyComment(
            @PathVariable @Min(value = 1, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId){
        Integer userId = LocalUser.getUser().getId();
        UserComments userComment = commentService.getUserValidComment(userId, Integer.parseInt(movieId));
        return new MyCommentVO(userComment);
    }

    @ScopeLevel
    @PostMapping("/movie/{movieId}")
    public Object submitMyComment(
            @PathVariable @Min(value = 1, message = "电影ID不合法")
            @Max(value = 200000000, message = "电影ID不合法") String movieId,
            @RequestParam String score, @RequestParam @Length(max = 512) String comment) {

        Integer userId = LocalUser.getUser().getId();
        Byte isCritic = LocalUser.getUser().getIsCritic();
        Integer movieIdInt = Integer.parseInt(movieId);
        Map<String, Object> map = new HashMap<>();
        int ret;

        if(score == null || score.equals("null")){
            ret = commentService.updateComment(userId, Integer.parseInt(movieId),null, comment);
            generateSubmitCommentResMap(map, ret, movieIdInt, userId, isCritic);
            return map;
        }

        if(score.equals("-1")){
            ret = commentService.deleteComment(userId, movieIdInt);
            generateSubmitCommentResMap(map, ret, movieIdInt, userId, isCritic);
            return map;
        }else{
            try{
                double scoreDouble = Double.parseDouble(score);
                if(scoreDouble > 10 || scoreDouble < 0){
                    throw new ParameterException(10001);
                }
                ret = commentService.updateComment(userId, Integer.parseInt(movieId), scoreDouble, comment);
            }catch (NumberFormatException e){
                throw new ParameterException(10001);
            }
        }
        generateSubmitCommentResMap(map, ret, movieIdInt, userId, isCritic);
        return map;
    }

    private void generateSubmitCommentResMap(Map<String, Object> map, Integer ret, Integer movieId, Integer userId, Byte isCritic){
        assert map != null;
        map.put("result", ret > 0);
        map.put("message", ret > 0? "操作成功": "操作失败");
        if(ret > 0){
            redisTemplate.opsForHash().delete(userStatKey, String.valueOf(userId));
            for(int i = 1; i <= 5; i++) {
                redisTemplate.delete(commentsCachedKey + ":" + userId + ":2:" + i);
                redisTemplate.delete(commentsCachedKey + ":" + userId + ":3:" + i);
            }

            movieUpdateMsgRabbitProducer.send(movieId + "-" + isCritic, exchange, routingKey);
        }
    }
}
