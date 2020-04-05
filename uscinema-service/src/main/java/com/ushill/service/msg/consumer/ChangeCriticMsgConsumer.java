package com.ushill.service.msg.consumer;

import com.rabbitmq.client.Channel;
import com.ushill.DTO.UserCommentSummaryDTO;
import com.ushill.msg.producer.RabbitProducer;
import com.ushill.service.interfaces.RatingService;
import com.ushill.service.interfaces.comments.UserCommentsService;
import com.ushill.utils.PagedGridResult;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午4:44
 */

@Component
public class ChangeCriticMsgConsumer {

    private final UserCommentsService userCommentsService;
    private final RabbitProducer movieUpdateMsgRabbitProducer;

    @Value("${uscinema.message.update-rating.ex}")
    private String updateRatingExchange;

    @Value("${uscinema.message.update-rating.rk}")
    private String updateRatingRoutingKey;

    public ChangeCriticMsgConsumer(UserCommentsService userCommentsService,
                                   RabbitProducer movieUpdateMsgRabbitProducer) {
        this.userCommentsService = userCommentsService;
        this.movieUpdateMsgRabbitProducer = movieUpdateMsgRabbitProducer;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${uscinema.message.change-critic.queue}", durable = "true"),
            exchange = @Exchange(value = "${uscinema.message.change-critic.ex}", durable = "true", type = "direct",
                    ignoreDeclarationExceptions = "true"),
            key = "${uscinema.message.change-critic.rk}"))
    @RabbitHandler
    public void onMessage(Message<Object> message, Channel channel) throws IOException, InterruptedException {
        System.out.println("收到消息：" + message.getPayload());
        String msg = String.valueOf(message.getPayload());
        assert msg != null;

        int userId = Integer.parseInt(msg);
        assert userId > 0;

        // 查询该用户评论过的电影，发送更新消息
        int page = 1;
        PagedGridResult<UserCommentSummaryDTO> paged;
        List<UserCommentSummaryDTO> comments;

        do{
            paged = userCommentsService.getUserCommentsSummary(userId, page);
            if(paged == null || paged.getRows() == null){
                break;
            }

            comments = paged.getRows();
            comments.forEach(comment -> {
                movieUpdateMsgRabbitProducer.send(comment.getMovieId() + "-1" , updateRatingExchange, updateRatingRoutingKey);
                movieUpdateMsgRabbitProducer.send(comment.getMovieId() + "-0" , updateRatingExchange, updateRatingRoutingKey);
            });
//            Thread.sleep(100);
            page += 1;
        }while (page <= paged.getTotal());

        Long deliverTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        assert deliverTag != null;

        channel.basicAck(deliverTag, false);
    }
}
