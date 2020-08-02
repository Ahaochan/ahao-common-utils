package com.ahao.spring.web.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MappingJackson2HttpMessageConverterRegister implements BeanDefinitionRegistryPostProcessor {
    public static Map<PropertyNamingStrategy, MediaType> strategyMediaTypeMap = new HashMap<PropertyNamingStrategy, MediaType>() {{
        put(PropertyNamingStrategy.SNAKE_CASE,       new MediaType("application", "vnd.snake.case+json"));
        put(PropertyNamingStrategy.UPPER_CAMEL_CASE, new MediaType("application", "vnd.upper.camel.case+json"));
        put(PropertyNamingStrategy.LOWER_CAMEL_CASE, new MediaType("application", "vnd.lower.camel.case+json"));
        put(PropertyNamingStrategy.LOWER_CASE,       new MediaType("application", "vnd.lower.case+json"));
        put(PropertyNamingStrategy.KEBAB_CASE,       new MediaType("application", "vnd.kebab+json"));
        put(PropertyNamingStrategy.LOWER_DOT_CASE,   new MediaType("application", "vnd.lower.dot+json"));
    }};

    private ObjectMapper sourceObjectMapper;
    public MappingJackson2HttpMessageConverterRegister(ObjectMapper objectMapper) {
        this.sourceObjectMapper = objectMapper;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (Map.Entry<PropertyNamingStrategy, MediaType> entry : strategyMediaTypeMap.entrySet()) {
            PropertyNamingStrategy strategy = entry.getKey();
            MediaType mediaType = entry.getValue();
            String beanName = strategy.getClass().getSimpleName() + MappingJackson2HttpMessageConverter.class.getSimpleName();

            ObjectMapper clone = this.sourceObjectMapper.copy();
            clone.setPropertyNamingStrategy(strategy);

            BeanDefinition beanDefinition = BeanDefinitionBuilder
                .genericBeanDefinition(MappingJackson2HttpMessageConverter.class, () -> createBean(clone, mediaType))
                .getBeanDefinition();

            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private MappingJackson2HttpMessageConverter createBean(ObjectMapper objectMapper, MediaType mediaType) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        converter.setSupportedMediaTypes(Collections.singletonList(mediaType));
        return converter;
    }
}
