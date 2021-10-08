package moe.ahao.transmit;

import feign.Feign;
import moe.ahao.transmit.filter.TransmitFilter;
import moe.ahao.transmit.interceptor.TransmitClientHttpRequestInterceptor;
import moe.ahao.transmit.interceptor.TransmitFeignRequestInterceptor;
import moe.ahao.transmit.interceptor.TransmitKafkaProducerInterceptor;
import moe.ahao.transmit.interceptor.TransmitRabbitMessagePostProcessor;
import moe.ahao.transmit.properties.TransmitProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "ahao.transmit", name = "enable", matchIfMissing = true)
@EnableConfigurationProperties(TransmitProperties.class)
public class TransmitterAutoConfig {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class ServletFilterConfig {
        @Bean
        @ConditionalOnMissingFilterBean(TransmitFilter.class)
        public FilterRegistrationBean<TransmitFilter> transmitFilter(TransmitProperties properties) {
            FilterRegistrationBean<TransmitFilter> bean = TransmitFilter.buildFilterBean(properties, "/*");
            return bean;
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(RestTemplate.class)
    public static class TransmitRestTemplateConfig {

        @Autowired(required = false)
        private List<RestTemplate> restTemplates = Collections.emptyList();

        @Bean
        @ConditionalOnMissingBean
        public SmartInitializingSingleton transmitClientHttpRequestInterceptorInit(TransmitClientHttpRequestInterceptor interceptor) {
            return () -> {
                for (RestTemplate restTemplate : restTemplates) {
                    List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
                    interceptors.add(interceptor);
                    restTemplate.setInterceptors(interceptors);
                }
            };
        }

        @Bean
        @ConditionalOnMissingBean
        public TransmitClientHttpRequestInterceptor transmitClientHttpRequestInterceptor(TransmitProperties properties) {
            return new TransmitClientHttpRequestInterceptor(properties);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(Feign.class)
    public static class TransmitFeignConfig {
        @Bean
        @ConditionalOnMissingBean
        public TransmitFeignRequestInterceptor transmitFeignRequestInterceptor(TransmitProperties properties) {
            return new TransmitFeignRequestInterceptor(properties);
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(RabbitTemplate.class)
    // 依赖链: RabbitTemplate -> RabbitBeanPostProcessor -> MessageProcessorCollector -> Before||After
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
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(KafkaTemplate.class)
    public static class TransmitKafkaConfig {
        @Bean
        @ConditionalOnMissingBean
        public TransmitKafkaProducerInterceptor transmitKafkaProducerInterceptor(TransmitProperties properties) {
            return new TransmitKafkaProducerInterceptor(properties);
        }
    }
}
