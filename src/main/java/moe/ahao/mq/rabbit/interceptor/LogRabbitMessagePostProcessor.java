package moe.ahao.mq.rabbit.interceptor;

import moe.ahao.mq.rabbit.processor.RabbitCollector;
import moe.ahao.util.commons.lang.time.DateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

/**
 * RabbitMQ 消息日志处理器
 */
public abstract class LogRabbitMessagePostProcessor implements MessagePostProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LogRabbitMessagePostProcessor.class);

    /**
     * 发送消息拦截器
     */
    public static class LogSend extends LogRabbitMessagePostProcessor implements RabbitCollector.Before {
        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            logger.debug("RabbitMQ发送消息: [{}], 消息发送时间:{}", message.toString(), DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS));
            return message;
        }
    }

    /**
     * 接收消息拦截器
     */
    public static class LogReceive extends LogRabbitMessagePostProcessor implements RabbitCollector.After {
        @Override
        public Message postProcessMessage(Message message) throws AmqpException {
            logger.debug("RabbitMQ接收消息: [{}], 消息接收时间:{}", message.toString(), DateHelper.getNow(DateHelper.yyyyMMdd_hhmmssSSS));
            return message;
        }
    }
}
