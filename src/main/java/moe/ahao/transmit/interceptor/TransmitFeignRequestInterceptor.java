package moe.ahao.transmit.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import moe.ahao.transmit.properties.TransmitProperties;
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
        transmitProperties.apply(requestTemplate::header);
    }
}
