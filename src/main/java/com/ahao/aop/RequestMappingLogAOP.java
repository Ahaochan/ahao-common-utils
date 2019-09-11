package com.ahao.aop;

import com.ahao.util.commons.lang.BeanHelper;
import com.ahao.util.commons.lang.reflect.ClassHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
//@Component
public class RequestMappingLogAOP {
    private static final Logger logger = LoggerFactory.getLogger(RequestMappingLogAOP.class);

    @Around("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        // 1. 根据切面参数, 获取 Redis 的 Key
        Object[] args = Arrays.stream(pjp.getArgs())
            .filter(a -> !ClassHelper.isSubClass(a, ServletRequest.class, ServletResponse.class, MultipartFile.class))
            .toArray(Object[]::new);
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        // 2. 统计耗时
        logger.info("{}.{}方法开始: 请求参数:{} ", className, methodName, BeanHelper.obj2JsonString(args));
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long end = System.currentTimeMillis();
        logger.info("{}.{}方法结束: 返回值:{}, 耗时:{}ms", className, methodName, BeanHelper.obj2JsonString(result), end - start);
        return result;
    }
}
