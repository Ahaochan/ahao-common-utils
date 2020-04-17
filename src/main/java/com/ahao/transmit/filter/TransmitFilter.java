package com.ahao.transmit.filter;

import com.ahao.transmit.properties.TransmitProperties;
import com.ahao.transmit.util.TransmitContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TransmitFilter extends OncePerRequestFilter {

    private TransmitProperties transmitProperties;
    public TransmitFilter(TransmitProperties transmitProperties) {
        this.transmitProperties = transmitProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        for (String header : transmitProperties.getHeaders()) {
            String value = getValue(request, header);
            TransmitContextHolder.set(header, value);
        }

        filterChain.doFilter(request, response);

        for (String header : transmitProperties.getHeaders()) {
            TransmitContextHolder.remove(header);
        }
    }

    private String getValue(HttpServletRequest request, String key) {
        String value = request.getHeader(key);
        if(StringUtils.isBlank(value)) {
            value = request.getParameter(key);
        }
        return StringUtils.defaultString(value, "");
    }

    public static FilterRegistrationBean<TransmitFilter> buildFilterBean(TransmitProperties properties, String... urlPatterns) {
        FilterRegistrationBean<TransmitFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TransmitFilter(properties));
        registrationBean.addUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
