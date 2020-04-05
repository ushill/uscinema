package com.ushill.msg.producer;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/3 下午5:32
 */

@Component
public class RabbitProducer {

    private final RabbitTemplate rabbitTemplate;

    private ThreadLocal<SimpleDateFormat> dateFormatLocal = ThreadLocal.withInitial(
            ()->new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"));

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//            if (!ack) {
//                System.out.println("Message NACK! " + correlationData + " cause: " + cause);
//            }else {
//                System.out.println("Message ACK! " + correlationData);
//            }
        }
    };

    final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
        @Override
        public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText,
                                    String exchange, String routingKey) {
            System.out.printf("Message %s returned. Code: %d, Text: %s, EX: %s, RK: %s",
                    message, replyCode, replyText, exchange, routingKey);
        }
    };

    public RabbitProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(Object message, Map<String, Object> properties, String exchange, String routingKey) {
        MessageHeaders mhs = new MessageHeaders(properties);
        Message<Object> msg = MessageBuilder.createMessage(message, mhs);

        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnCallback);

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, msg, correlationData);
    }

    public void send(Object message, String exchange, String routingKey) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("send_time", dateFormatLocal.get().format(new Date()));
        send(message, properties, exchange, routingKey);
    }
}