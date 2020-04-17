package com.ahao.transmit.properties;

import com.ahao.transmit.util.TransmitContextHolder;

import java.util.ArrayList;
import java.util.List;

public class TransmitProperties {
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
}
