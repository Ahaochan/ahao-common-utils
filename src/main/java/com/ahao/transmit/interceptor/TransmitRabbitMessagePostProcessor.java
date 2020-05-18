package com.ahao.transmit.interceptor;

import com.ahao.mq.rabbit.processor.MessageProcessorCollector;
import com.ahao.transmit.properties.TransmitProperties;
import com.ahao.transmit.util.TransmitContextHolder;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

import java.util.Map;

/**
 * RabbitMQ 消息透传处理器
 */
public abstract class TransmitRabbitMessagePostProcessor implements MessagePostProcessor {
    protected TransmitProperties transmitProperties;

    public TransmitRabbitMessagePostProcessor(TransmitProperties transmitProperties) {
        this.transmitProperties = transmitProperties;
    }

    /**
     * 发送消息拦截器
     */
    public static class Before extends TransmitRabbitMessagePostProcessor implements MessageProcessorCollector.Before {
        public Before(TransmitProperties transmitProperties) {
            super(transmitProperties);
        }

        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            MessageProperties properties = message.getMessageProperties();
            transmitProperties.apply(properties::setHeader);
            return message;
        }
    }

    /**
     * 接收消息拦截器
     */
    public static class After extends TransmitRabbitMessagePostProcessor implements MessageProcessorCollector.After {
        public After(TransmitProperties transmitProperties) {
            super(transmitProperties);
        }

        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            MessageProperties properties = message.getMessageProperties();
            Map<String, Object> headers = properties.getHeaders();
            headers.forEach((key, value) -> TransmitContextHolder.set(key, value == null ? null : value.toString()));
            return message;
        }
    }
}
