package moe.ahao.elastic.elasticsearch;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestClientBuilder.class)
public class ElasticsearchConfig {
    /**
     * 建立连接超时时间
     */
    public static int CONNECT_TIMEOUT_MILLIS = 1000;
    /**
     * 数据传输过程中的超时时间
     */
    public static int SOCKET_TIMEOUT_MILLIS = 30000;
    /**
     * 从连接池获取连接的超时时间
     */
    public static int CONNECTION_REQUEST_TIMEOUT_MILLIS = 500;
    /**
     * 路由节点的最大连接数
     */
    public static int MAX_CONN_PER_ROUTE = 10;
    /**
     * client最大连接数量
     */
    public static int MAX_CONN_TOTAL = 30;

    @Bean
    RestClientBuilderCustomizer requestConfigCallback() {
        return new RestClientBuilderCustomizer() {
            @Override
            public void customize(RestClientBuilder builder) {

            }

            @Override
            public void customize(RequestConfig.Builder builder) {
                builder.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
                builder.setSocketTimeout(SOCKET_TIMEOUT_MILLIS);
                builder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT_MILLIS);
            }

            @Override
            public void customize(HttpAsyncClientBuilder builder) {
                builder.setMaxConnTotal(MAX_CONN_TOTAL);
                builder.setMaxConnPerRoute(MAX_CONN_PER_ROUTE);
            }
        };
    }
}
