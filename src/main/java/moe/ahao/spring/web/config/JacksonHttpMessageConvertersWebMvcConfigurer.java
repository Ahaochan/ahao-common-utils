package moe.ahao.spring.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class JacksonHttpMessageConvertersWebMvcConfigurer {

    /**
     * 覆盖 {@link JacksonHttpMessageConvertersConfiguration} 的配置, 移除 application/*+json 的支持
     * <p>
     * http://blog.ahao.moe/posts/Support_both_snake_case_and_camelCase_in_Spring_MVC.html
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
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter1(ObjectMapper objectMapper) {
        return PropertyNamingStrategyConverter.SNAKE_CASE.createBean(objectMapper);
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter2(ObjectMapper objectMapper) {
        return PropertyNamingStrategyConverter.UPPER_CAMEL_CASE.createBean(objectMapper);
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter3(ObjectMapper objectMapper) {
        return PropertyNamingStrategyConverter.LOWER_CAMEL_CASE.createBean(objectMapper);
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter4(ObjectMapper objectMapper) {
        return PropertyNamingStrategyConverter.LOWER_CASE.createBean(objectMapper);
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter5(ObjectMapper objectMapper) {
        return PropertyNamingStrategyConverter.KEBAB_CASE.createBean(objectMapper);
    }
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter6(ObjectMapper objectMapper) {
        return PropertyNamingStrategyConverter.LOWER_DOT_CASE.createBean(objectMapper);
    }

    @Getter
    @AllArgsConstructor
    public enum PropertyNamingStrategyConverter {
        SNAKE_CASE(PropertyNamingStrategy.SNAKE_CASE, new MediaType("application", "vnd.snake.case+json")),
        UPPER_CAMEL_CASE(PropertyNamingStrategy.UPPER_CAMEL_CASE, new MediaType("application", "vnd.upper.camel.case+json")),
        LOWER_CAMEL_CASE(PropertyNamingStrategy.LOWER_CAMEL_CASE, new MediaType("application", "vnd.lower.camel.case+json")),
        LOWER_CASE(PropertyNamingStrategy.LOWER_CASE, new MediaType("application", "vnd.lower.case+json")),
        KEBAB_CASE(PropertyNamingStrategy.KEBAB_CASE, new MediaType("application", "vnd.kebab+json")),
        LOWER_DOT_CASE(PropertyNamingStrategy.LOWER_DOT_CASE, new MediaType("application", "vnd.lower.dot+json")),
        ;
        private final PropertyNamingStrategy strategy;
        private final MediaType mediaType;

        public MappingJackson2HttpMessageConverter createBean(ObjectMapper om) {
            ObjectMapper objectMapper = om.copy();
            objectMapper.setPropertyNamingStrategy(strategy);

            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
            converter.setSupportedMediaTypes(Collections.singletonList(mediaType));
            return converter;
        }
    }
}
