package moe.ahao.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HystrixCacheFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            filterChain.doFilter(request, response);
        } finally {
            context.shutdown();
        }
    }

    public static FilterRegistrationBean<HystrixCacheFilter> buildFilterBean(String... urlPatterns) {
        FilterRegistrationBean<HystrixCacheFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new HystrixCacheFilter());
        registrationBean.addUrlPatterns(urlPatterns);
        return registrationBean;
    }
}
