package com.ahao.mq.rabbit;

import com.ahao.mq.rabbit.convert.JsonMessageConverter;
import com.ahao.mq.rabbit.processor.MessageProcessorCollector;
import com.ahao.mq.rabbit.processor.RabbitBeanPostProcessor;
import com.ahao.util.spring.mq.RabbitMQHelper;
import com.rabbitmq.client.Channel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({ RabbitTemplate.class, Channel.class })
@ConditionalOnProperty(prefix = "spring.rabbitmq", value = "host")
@EnableConfigurationProperties(RabbitProperties.class)

@EnableRabbit
public class RabbitAutoConfig {

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
    public MessageProcessorCollector messageProcessorCollector(ObjectProvider<List<MessageProcessorCollector.Before>> beforeListProvider,
                                                               ObjectProvider<List<MessageProcessorCollector.After>> afterListProvider) {
        List<MessagePostProcessor> beforeList = beforeListProvider.stream().map(s -> (MessagePostProcessor) s).collect(Collectors.toList());
        List<MessagePostProcessor> afterList =  afterListProvider.stream().map(s -> (MessagePostProcessor) s).collect(Collectors.toList());

        MessageProcessorCollector collector = new MessageProcessorCollector();
        collector.setTemplateBeforeMessagePostProcessorList(beforeList);
        collector.setTemplateAfterMessagePostProcessorList(afterList);
        collector.setFactoryBeforeMessagePostProcessorList(beforeList);
        collector.setFactoryAfterMessagePostProcessorList(afterList);
        return collector;
    }

    @Bean
    @ConditionalOnMissingBean
    public RabbitBeanPostProcessor rabbitBeanPostProcessor(MessageProcessorCollector collector) {
        RabbitBeanPostProcessor rabbitBeanPostProcessor = new RabbitBeanPostProcessor(collector);
        return rabbitBeanPostProcessor;
    }

}
