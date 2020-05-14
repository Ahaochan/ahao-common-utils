package com.ahao.transmit.configuration;

import com.ahao.transmit.filter.TransmitFilter;
import com.ahao.transmit.interceptor.TransmitClientHttpRequestInterceptor;
import com.ahao.transmit.interceptor.TransmitFeignRequestInterceptor;
import com.ahao.transmit.interceptor.TransmitRabbitMessagePostProcessor;
import com.ahao.transmit.properties.TransmitProperties;
import feign.Feign;
import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "ahao.transmit", name = "enable")
public class EnableTransmitterAutoConfiguration {

    @Bean
    @ConfigurationProperties("ahao.transmit")
    public TransmitProperties transmitProperties() {
        return new TransmitProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<TransmitFilter> transmitFilter(TransmitProperties properties) {
        FilterRegistrationBean<TransmitFilter> bean = TransmitFilter.buildFilterBean(properties, "/*");
        return bean;
    }

    @Configuration
    @ConditionalOnBean(RestTemplate.class)
    public static class TransmitRestTemplateConfig {
        @Bean
        @ConditionalOnMissingBean
        public RestTemplateCustomizer restTemplateCustomizer(TransmitClientHttpRequestInterceptor interceptor) {
            return restTemplate -> {
                List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
                list.add(interceptor);
                restTemplate.setInterceptors(list);
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public TransmitClientHttpRequestInterceptor transmitClientHttpRequestInterceptor(TransmitProperties properties) {
            return new TransmitClientHttpRequestInterceptor(properties);
        }
    }

    @Configuration
    @ConditionalOnClass(Feign.class)
    public static class TransmitFeignConfig {
        @Bean
        @ConditionalOnMissingBean
        public TransmitFeignRequestInterceptor transmitFeignRequestInterceptor(TransmitProperties properties) {
            return new TransmitFeignRequestInterceptor(properties);
        }
    }

    @Configuration
    @ConditionalOnBean(RabbitTemplate.class)
    public static class TransmitRabbitConfig {
        @Bean
        @ConditionalOnMissingBean
        public TransmitRabbitMessagePostProcessor.Before transmitRabbitBeforeMessagePostProcessor(TransmitProperties properties) {
            return new TransmitRabbitMessagePostProcessor.Before(properties);
        }

        @Bean
        @ConditionalOnMissingBean
        public TransmitRabbitMessagePostProcessor.After transmitRabbitAfterMessagePostProcessor(TransmitProperties properties) {
            return new TransmitRabbitMessagePostProcessor.After(properties);
        }

        @Bean
        @ConditionalOnMissingBean
        public BeanPostProcessor rabbitBeanPostProcessor(TransmitProperties properties) {
            return new BeanPostProcessor() {
                @Override
                public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                    if(bean instanceof RabbitTemplate) {
                        RabbitTemplate rabbitTemplate = (RabbitTemplate) bean;
                        rabbitTemplate.setAfterReceivePostProcessors(transmitRabbitAfterMessagePostProcessor(properties));
                        rabbitTemplate.setBeforePublishPostProcessors(transmitRabbitBeforeMessagePostProcessor(properties));
                    }
                    if (bean instanceof AbstractRabbitListenerContainerFactory) {
                        AbstractRabbitListenerContainerFactory<?> factory = (AbstractRabbitListenerContainerFactory<?>) bean;
                        factory.setAfterReceivePostProcessors(transmitRabbitAfterMessagePostProcessor(properties));
                        factory.setBeforeSendReplyPostProcessors(transmitRabbitBeforeMessagePostProcessor(properties));
                    }
                    return bean;
                }
            };
        }
    }
}
