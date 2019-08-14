package com.ahao.util.spring.mq;

import com.ahao.util.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.io.Serializable;
import java.util.UUID;

public class RabbitMQHelper {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQHelper.class);
    /**
     * 延迟消息交换机
     */
    public static final String DELAY_EXCHANGE_NAME = "delay_exchange";

    // ======================================== 依赖 ==================================================
    private volatile static RabbitAdmin rabbitAdmin;
    private volatile static RabbitTemplate rabbitTemplate;
    private volatile static Exchange delayExchange;
    public static RabbitTemplate getRabbitTemplate() {
        if(rabbitTemplate == null) {
            synchronized (RabbitMQHelper.class) {
                if(rabbitTemplate == null) {
                    init();
                }
            }
        }
        return rabbitTemplate;
    }
    public static RabbitAdmin getRabbitAdmin() {
        if(rabbitAdmin == null) {
            synchronized (RabbitMQHelper.class) {
                if(rabbitAdmin == null) {
                    init();
                }
            }
        }
        return rabbitAdmin;
    }
    public static Exchange getDelayExchange() {
        if(delayExchange == null) {
            synchronized (RabbitMQHelper.class) {
                if(delayExchange == null) {
                    init();
                }
            }
        }
        return delayExchange;
    }
    private static void init() {
        RabbitMQHelper.rabbitTemplate = SpringContextHolder.getBean(RabbitTemplate.class);
        RabbitMQHelper.rabbitAdmin = SpringContextHolder.getBean(RabbitAdmin.class);
        RabbitMQHelper.delayExchange = SpringContextHolder.getBean(DELAY_EXCHANGE_NAME);
    }
    // ======================================== 依赖 ==================================================

    public static void send(String queueName, Serializable data) {
        doSend(queueName, data);
    }
    public static void send(String queueName, String data) {
        doSend(queueName, data);
    }
    public static void send(String queueName, byte[] data) {
        doSend(queueName, data);
    }
    private static void doSend(String queueName, Object data) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        getRabbitTemplate().convertAndSend(queueName, data, correlationId);
    }

    public static void sendDelay(String queueName, Serializable data, long delayMilliSeconds) throws IllegalArgumentException {
        doSendDelay(queueName, data, delayMilliSeconds);
    }
    public static void sendDelay(String queueName, String data, long delayMilliSeconds) throws IllegalArgumentException {
        doSendDelay(queueName, data, delayMilliSeconds);
    }
    public static void sendDelay(String queueName, byte[] data, long delayMilliSeconds) throws IllegalArgumentException {
        doSendDelay(queueName, data, delayMilliSeconds);
    }
    private static void doSendDelay(String queueName, Object data, long delayMilliSeconds) throws IllegalArgumentException {
        if(delayMilliSeconds > 0xffffffffL) {
            throw new IllegalArgumentException("超时过长, 只支持 < 4294967296 的延时值");
        }
        Binding binding = BindingBuilder.bind(new Queue(queueName)).to(getDelayExchange()).with(queueName).noargs();
        getRabbitAdmin().declareBinding(binding);

        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        getRabbitTemplate().convertAndSend(RabbitMQHelper.DELAY_EXCHANGE_NAME, queueName, data, message -> {
            MessageProperties messageProperties = message.getMessageProperties();
            messageProperties.getHeaders().put("x-delay", delayMilliSeconds);
            return message;
        }, correlationId);
    }
}
