package com.ahao.transmit.configuration;

import com.ahao.transmit.filter.TransmitFilter;
import com.ahao.transmit.interceptor.TransmitFeignRequestInterceptor;
import com.ahao.transmit.properties.TransmitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public TransmitFeignRequestInterceptor transmitFeignRequestInterceptor(TransmitProperties properties) {
        return new TransmitFeignRequestInterceptor(properties);
    }
}
