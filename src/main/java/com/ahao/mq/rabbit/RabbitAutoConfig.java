package com.ahao.mq.rabbit;

import com.ahao.mq.rabbit.convert.JsonMessageConverter;
import com.ahao.mq.rabbit.processor.RabbitBeanPostProcessor;
import com.ahao.mq.rabbit.processor.RabbitCollector;
import com.ahao.util.spring.mq.RabbitMQHelper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({RabbitTemplate.class, Channel.class})
@ConditionalOnProperty(prefix = "spring.rabbitmq", value = "host")
@EnableConfigurationProperties(RabbitProperties.class)

@EnableRabbit
public class RabbitAutoConfig {
    public static final Logger logger = LoggerFactory.getLogger(RabbitAutoConfig.class);

    @Bean
    @ConditionalOnMissingBean
    public MessageConverter messageConverter() {
        JsonMessageConverter converter = new JsonMessageConverter();
        converter.setCharset(StandardCharsets.UTF_8);
        converter.setUseRawJson(false);
        return converter;
    }

    @Bean(RabbitMQHelper.DELAY_EXCHANGE_NAME)
    public Exchange delayExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(RabbitMQHelper.DELAY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return (data, ack, cause) -> {
            if (!ack) {
                logger.error("消息[{}]发送失败, cause:{}", data, cause);
            } else {
                logger.error("消息[{}]发送成功, cause:{}", data, cause);
            }
        };
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitTemplate.ReturnCallback returnCallback() {
        return (message, replyCode, replyText, exchange, routingKey) ->
            logger.info(MessageFormat.format("消息发送ReturnCallback:{0},{1},{2},{3},{4},{5}",
                message, replyCode, replyText, exchange, routingKey));
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitCollector messageProcessorCollector(ObjectProvider<List<RabbitCollector.Before>> beforeListProvider,
                                                     ObjectProvider<List<RabbitCollector.After>> afterListProvider) {
        List<MessagePostProcessor> beforeList = beforeListProvider.stream().map(s -> (MessagePostProcessor) s).collect(Collectors.toList());
        List<MessagePostProcessor> afterList = afterListProvider.stream().map(s -> (MessagePostProcessor) s).collect(Collectors.toList());

        RabbitCollector collector = new RabbitCollector();
        collector.setTemplateBeforeMessagePostProcessorList(beforeList);
        collector.setTemplateAfterMessagePostProcessorList(afterList);
        collector.setFactoryBeforeMessagePostProcessorList(beforeList);
        collector.setFactoryAfterMessagePostProcessorList(afterList);
        return collector;
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitBeanPostProcessor rabbitBeanPostProcessor(RabbitCollector collector) {
        RabbitBeanPostProcessor rabbitBeanPostProcessor = new RabbitBeanPostProcessor(collector);
        return rabbitBeanPostProcessor;
    }

}
