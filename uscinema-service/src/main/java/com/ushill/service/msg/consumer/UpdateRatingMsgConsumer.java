package com.ushill.service.msg.consumer;

import com.rabbitmq.client.Channel;
import com.ushill.service.interfaces.RatingService;
import com.ushill.utils.BoolUtils;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午4:44
 */

@Component
public class UpdateRatingMsgConsumer {

    private final RatingService ratingService;
    private final StringRedisTemplate redisTemplate;

    @Value("${uscinema.cached-key.v1.movie.detail}")
    private String movieCachedKey;

    @Value("${uscinema.cached-key.v1.movie.comment-ratio}")
    private String commentsRatioCachedKey;

    @Value("${uscinema.cached-key.v1.movie.comments}")
    private String commentsCachedKey;

    public UpdateRatingMsgConsumer(RatingService ratingService, StringRedisTemplate redisTemplate) {
        this.ratingService = ratingService;
        this.redisTemplate = redisTemplate;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${uscinema.message.update-rating.queue}", durable = "true"),
            exchange = @Exchange(value = "${uscinema.message.update-rating.ex}", durable = "true", type = "direct",
                    ignoreDeclarationExceptions = "true"),
            key = "${uscinema.message.update-rating.rk}"))
    @RabbitHandler
    public void onMessage(Message<Object> message, Channel channel) throws IOException, InterruptedException {
//        System.out.println("收到消息：" + message.getPayload());
        String msg = String.valueOf(message.getPayload());
        assert msg != null;

        String[] arr = msg.trim().split("-");
        assert arr.length == 2;

        int movieId = Integer.parseInt(arr[0]);
        int isCritic = Integer.parseInt(arr[1]);
        assert movieId > 0;

        // 更新电影评分
        int ret = ratingService.updateMovieRating(movieId, isCritic != 0);

        // 删除电影相关缓存
        redisTemplate.delete(movieCachedKey + ":" + movieId);
        redisTemplate.delete(commentsRatioCachedKey + ":" + movieId + ":" + isCritic);
        if(!redisTemplate.hasKey(commentsCachedKey + ":" + movieId + ":" + isCritic + ":" + 3)) {
            redisTemplate.delete(commentsCachedKey + ":" + movieId + ":" + isCritic + ":" + 1);
            redisTemplate.delete(commentsCachedKey + ":" + movieId + ":" + isCritic + ":" + 2);
        }

        System.out.println(movieId + "-" + isCritic + "已更新");
//        Thread.sleep(10);

        Long deliverTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        assert deliverTag != null;

        channel.basicAck(deliverTag, false);
    }
}
