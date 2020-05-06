package com.ahao.transmit.interceptor;

import com.ahao.transmit.properties.TransmitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class TransmitClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {
    private static Logger logger = LoggerFactory.getLogger(TransmitClientHttpRequestInterceptor.class);

    private TransmitProperties transmitProperties;
    public TransmitClientHttpRequestInterceptor(TransmitProperties transmitProperties) {
        this.transmitProperties = transmitProperties;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        transmitProperties.apply(h -> headers.add(h.getName(), h.getValue()));
        return execution.execute(request, body);
    }
}
