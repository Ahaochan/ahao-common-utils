package moe.ahao.sentinel;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import moe.ahao.sentinel.handler.CommonBlockExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(SentinelProperties.class)
public class SentinelAutoConfig {
    @Bean
    public BlockExceptionHandler blockExceptionHandler(ObjectMapper objectMapper) {
        CommonBlockExceptionHandler handler = new CommonBlockExceptionHandler();
        handler.setObjectMapper(objectMapper);
        return handler;
    }
}
