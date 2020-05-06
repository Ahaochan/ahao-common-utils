package com.ahao.transmit.interceptor;

import com.ahao.transmit.properties.TransmitProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransmitFeignRequestInterceptor implements RequestInterceptor {
    private static Logger logger = LoggerFactory.getLogger(TransmitFeignRequestInterceptor.class);

    private TransmitProperties transmitProperties;
    public TransmitFeignRequestInterceptor(TransmitProperties transmitProperties) {
        this.transmitProperties = transmitProperties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        transmitProperties.apply(h -> requestTemplate.header(h.getName(), h.getValue()));
    }
}
