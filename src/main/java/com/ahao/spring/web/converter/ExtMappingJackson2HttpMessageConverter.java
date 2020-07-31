package com.ahao.spring.web.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

public class ExtMappingJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    public ExtMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        this(objectMapper, MediaType.APPLICATION_JSON);
    }

    public ExtMappingJackson2HttpMessageConverter(ObjectMapper objectMapper, MediaType... supportedMediaTypes) {
        super(objectMapper, supportedMediaTypes);
    }

    public static MediaType createMediaType(PropertyNamingStrategy strategy) {
        if (strategy == PropertyNamingStrategy.SNAKE_CASE) {
            return new MediaType("application", "vnd.snake.case+json");
        }
        if (strategy == PropertyNamingStrategy.UPPER_CAMEL_CASE) {
            return new MediaType("application", "vnd.upper.camel.case+json");
        }
        if (strategy == PropertyNamingStrategy.LOWER_CAMEL_CASE) {
            return new MediaType("application", "vnd.lower.camel.case+json");
        }
        if (strategy == PropertyNamingStrategy.LOWER_CASE) {
            return new MediaType("application", "vnd.lower.case+json");
        }
        if (strategy == PropertyNamingStrategy.KEBAB_CASE) {
            return new MediaType("application", "vnd.kebab+json");
        }
        if (strategy == PropertyNamingStrategy.LOWER_DOT_CASE) {
            return new MediaType("application", "vnd.lower.dot+json");
        }
        throw new UnsupportedOperationException("不支持 Jackson 命名策略: " + strategy.getClass().getSimpleName() + "生成 MediaType");
    }
}
