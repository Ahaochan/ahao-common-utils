package com.ahao.spring.web.config;

import com.ahao.spring.web.converter.MappingJackson2HttpMessageConverterRegister;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class JacksonHttpMessageConvertersWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 覆盖 {@link JacksonHttpMessageConvertersConfiguration} 的配置, 移除 application/*+json 的支持
     * @see JacksonHttpMessageConvertersConfiguration.MappingJackson2HttpMessageConverterConfiguration#mappingJackson2HttpMessageConverter(com.fasterxml.jackson.databind.ObjectMapper)
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        List<MediaType> mediaTypes = Arrays.asList(
            MediaType.APPLICATION_JSON
            // new MediaType("application", "*+json") // 移除 application/*+json 的支持
        );
        converter.setSupportedMediaTypes(mediaTypes);
        return converter;
    }

    @Bean
    public MappingJackson2HttpMessageConverterRegister mappingJackson2HttpMessageConverterRegister(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverterRegister register = new MappingJackson2HttpMessageConverterRegister(objectMapper);
        return register;
    }
}
