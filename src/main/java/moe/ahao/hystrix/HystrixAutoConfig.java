package moe.ahao.hystrix;

import com.netflix.hystrix.HystrixCommand;
import moe.ahao.transmit.filter.TransmitFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(HystrixCommand.class)
public class HystrixAutoConfig {
    @Bean
    public TransmitHystrixConcurrencyStrategy transmitHystrixConcurrencyStrategy() {
        return new TransmitHystrixConcurrencyStrategy();
    }

    @Bean
    @ConditionalOnMissingFilterBean(TransmitFilter.class)
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public FilterRegistrationBean<HystrixCacheFilter> hystrixCacheFilterFilterRegistrationBean() {
        FilterRegistrationBean<HystrixCacheFilter> bean = HystrixCacheFilter.buildFilterBean("/*");
        return bean;
    }
}
