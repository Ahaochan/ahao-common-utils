package moe.ahao.transmit.properties;

import moe.ahao.transmit.util.TransmitContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@ConfigurationProperties("cl.biz-common.transmit")
public class TransmitProperties {
    private static Logger logger = LoggerFactory.getLogger(TransmitProperties.class);
    private boolean enable = true;
    private List<String> headers = new ArrayList<String>(){{
            add(TransmitContextHolder.HEADER_TOKEN);
        }};

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void apply(BiConsumer<String, String> consumer) {
        logger.debug("Transmit透传开关:{}", enable);
        if(!enable) {
            return;
        }
        for (String header : headers) {
            String value = TransmitContextHolder.get(header);
            if(StringUtils.isBlank(value)) {
                continue;
            }
            logger.debug("Transmit透传拦截器注入请求头{}:{}", header, value);
            consumer.accept(header, value);
        }
    }

}
