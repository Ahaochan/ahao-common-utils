package com.ahao.spring.web.config;

import com.ahao.spring.web.converter.ExtMappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
public class JacksonHttpMessageConvertersWebMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

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

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 1. 找到插入位置
        List<MediaType> exceptMediaTypes = Collections.singletonList(MediaType.APPLICATION_JSON);
        int index = 0;
        for (int i = 0; i < converters.size(); i++) {
            HttpMessageConverter<?> converter = converters.get(i);
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                index = i;
                List<MediaType> supportedMediaTypes = converter.getSupportedMediaTypes();
                Assert.isTrue(Objects.equals(exceptMediaTypes, supportedMediaTypes), "第 1 个 MappingJackson2HttpMessageConverter 支持的 MediaType:" + supportedMediaTypes + "应为" + exceptMediaTypes);
                break;
            }
        }
        index++;

        // 2. 补充多种 PropertyNamingStrategy
        PropertyNamingStrategy[] strategies = {
            PropertyNamingStrategy.SNAKE_CASE,
            PropertyNamingStrategy.UPPER_CAMEL_CASE,
            PropertyNamingStrategy.LOWER_CAMEL_CASE,
            PropertyNamingStrategy.LOWER_CASE,
            PropertyNamingStrategy.KEBAB_CASE,
            PropertyNamingStrategy.LOWER_DOT_CASE};
        for (PropertyNamingStrategy strategy : strategies) {

            ObjectMapper clone = objectMapper.copy();
            clone.setPropertyNamingStrategy(strategy);

            MediaType mediaType = ExtMappingJackson2HttpMessageConverter.createMediaType(strategy);
            ExtMappingJackson2HttpMessageConverter converter = new ExtMappingJackson2HttpMessageConverter(clone, mediaType);

            converters.add(index, converter);
        }
    }
}
