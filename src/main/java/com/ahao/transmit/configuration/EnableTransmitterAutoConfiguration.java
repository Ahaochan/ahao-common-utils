package com.ahao.transmit.configuration;

import com.ahao.transmit.filter.TransmitFilter;
import com.ahao.transmit.interceptor.TransmitClientHttpRequestInterceptor;
import com.ahao.transmit.interceptor.TransmitFeignRequestInterceptor;
import com.ahao.transmit.properties.TransmitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

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
    public FilterRegistrationBean<TransmitFilter> transmitFilter(TransmitProperties properties) {
        FilterRegistrationBean<TransmitFilter> bean = TransmitFilter.buildFilterBean(properties, "/*");
        return bean;
    }

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer(TransmitClientHttpRequestInterceptor interceptor) {
        return restTemplate -> {
            List<ClientHttpRequestInterceptor> list = new ArrayList<>(restTemplate.getInterceptors());
            list.add(interceptor);
            restTemplate.setInterceptors(list);
        };
    }

    @Bean
    public TransmitFeignRequestInterceptor transmitFeignRequestInterceptor(TransmitProperties properties) {
        return new TransmitFeignRequestInterceptor(properties);
    }

    @Bean
    public TransmitClientHttpRequestInterceptor transmitClientHttpRequestInterceptor(TransmitProperties properties) {
        return new TransmitClientHttpRequestInterceptor(properties);
    }
}
