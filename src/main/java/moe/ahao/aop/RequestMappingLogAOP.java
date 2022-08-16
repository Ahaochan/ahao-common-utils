package moe.ahao.aop;

import moe.ahao.util.commons.io.JSONHelper;
import moe.ahao.util.commons.lang.reflect.ClassHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
//@Component
public class RequestMappingLogAOP {
    private static final Logger logger = LoggerFactory.getLogger(RequestMappingLogAOP.class);

    @Around("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = Arrays.stream(pjp.getArgs())
            .filter(a -> !ClassHelper.isSubClass(a, HttpSession.class, ServletRequest.class, ServletResponse.class, MultipartFile.class))
            .toArray(Object[]::new);
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();

        // 2. 统计耗时
        logger.info("{}.{}方法开始: 请求参数:{} ", className, methodName, JSONHelper.toString(args));
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            logger.error("{}.{}方法发生异常", className, methodName, e);
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            logger.info("{}.{}方法结束: 返回值:{}, 耗时:{}ms", className, methodName, JSONHelper.toString(result), end - start);
        }
        return result;
    }
}
