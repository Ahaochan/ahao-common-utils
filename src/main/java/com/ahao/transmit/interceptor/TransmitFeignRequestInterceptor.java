package com.ahao.transmit.interceptor;

import com.ahao.transmit.properties.TransmitProperties;
import com.ahao.transmit.util.TransmitContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TransmitFeignRequestInterceptor implements RequestInterceptor {
    private static Logger log = LoggerFactory.getLogger(TransmitFeignRequestInterceptor.class);

    private TransmitProperties transmitProperties;
    public TransmitFeignRequestInterceptor(TransmitProperties transmitProperties) {
        this.transmitProperties = transmitProperties;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        List<String> headers = transmitProperties.getHeaders();
        for (String header : headers) {
            String value = TransmitContextHolder.get(header);
            if(StringUtils.isBlank(value)) {
                continue;
            }
            requestTemplate.header(header, value);
        }
    }
}
