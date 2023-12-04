package moe.ahao.spring.web.xss;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new XSSRequestWrapper(request), response);
    }

    /**
     * 交由 @Configuration 将 Filter 注入到 Spring 容器中
     *
     * @param urlPattern Filter 匹配的 url
     */
    public static FilterRegistrationBean<XssFilter> buildFilterBean(String urlPattern) {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new XssFilter());
        registration.addUrlPatterns(urlPattern);
        registration.setName(XssFilter.class.getSimpleName());
        return registration;
    }


    public static class XSSRequestWrapper extends HttpServletRequestWrapper {
        public XSSRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);

            if (values == null) {
                return null;
            }

            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = this.stripXSS(values[i]);
            }

            return encodedValues;
        }

        @Override
        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            return this.stripXSS(value);
        }

        @Override
        public String getHeader(String name) {
            String value = super.getHeader(name);
            return this.stripXSS(value);
        }

        private String stripXSS(String value) {
            return StringEscapeUtils.escapeHtml4(value);
        }
    }
}
