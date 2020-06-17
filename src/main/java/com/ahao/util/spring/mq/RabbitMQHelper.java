package com.ahao.util.spring.mq;

import com.ahao.mq.rabbit.convert.JsonMessageConverter;
import com.ahao.util.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.PropertyMapper;

import java.time.Duration;
import java.util.UUID;

public class RabbitMQHelper {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMQHelper.class);
    /**
     * 延迟消息交换机
     */
    public static final String DELAY_EXCHANGE_NAME = "delay_exchange";

    // ======================================== 依赖 ==================================================
    private volatile static ConnectionFactory factory;
    private volatile static RabbitAdmin rabbitAdmin;
    private volatile static RabbitTemplate rabbitTemplate;
    private volatile static Exchange delayExchange;
    private static ConnectionFactory getFactory() {
        if(factory == null) {
            synchronized (RabbitMQHelper.class) {
                if(factory == null) {
                    init();
                }
            }
        }
        return factory;
    }
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
    private static Exchange getDelayExchange() {
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
        RabbitMQHelper.factory = SpringContextHolder.getBean(ConnectionFactory.class);
        RabbitMQHelper.rabbitTemplate = SpringContextHolder.getBean(RabbitTemplate.class);
        RabbitMQHelper.rabbitAdmin = SpringContextHolder.getBean(RabbitAdmin.class);
        RabbitMQHelper.delayExchange = SpringContextHolder.getBean(DELAY_EXCHANGE_NAME);
    }
    // ======================================== 依赖 ==================================================

    public static void send(String queueName, Object data) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        getRabbitTemplate().convertAndSend(queueName, data, correlationId);
    }

    public static void sendDelay(String queueName, Object data, long delayMilliSeconds) throws IllegalArgumentException {
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

    /**
     * 构建 RabbitTemplate
     * @see org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration.RabbitTemplateConfiguration#rabbitTemplate(RabbitProperties, ObjectProvider, ObjectProvider, ConnectionFactory)
     */
    public static RabbitTemplate withDefault(RabbitProperties properties) {
        PropertyMapper map = PropertyMapper.get();
        RabbitTemplate template = new RabbitTemplate(getFactory());

        template.setMessageConverter(new JsonMessageConverter());
        if(properties != null) {
            RabbitProperties.Template templateProperties = properties.getTemplate();

            map.from(templateProperties::getReceiveTimeout).whenNonNull().as(Duration::toMillis).to(template::setReceiveTimeout);
            map.from(templateProperties::getReplyTimeout).whenNonNull().as(Duration::toMillis).to(template::setReplyTimeout);
            map.from(templateProperties::getExchange).to(template::setExchange);
            map.from(templateProperties::getRoutingKey).to(template::setRoutingKey);
            map.from(templateProperties::getDefaultReceiveQueue).whenNonNull().to(template::setDefaultReceiveQueue);
        }
        return template;
    }
}
