package moe.ahao.dubbo.filter;


import lombok.extern.slf4j.Slf4j;
import moe.ahao.exception.BizException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.utils.ReflectUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

import java.lang.reflect.Method;


/**
 * ExceptionInvokerFilter
 * <p>
 * Functions:
 * <ol>
 * <li>unexpected exception will be logged in ERROR level on provider side. Unexpected exception are unchecked
 * exception not declared on the interface</li>
 * <li>Wrap the exception not introduced in API package into RuntimeException. Framework will serialize the outer exception but stringnize its cause in order to avoid of possible serialization problem on client side</li>
 * </ol>
 */
@Slf4j
@Activate(group = CommonConstants.PROVIDER)
public class DubboExceptionFilter implements Filter, Filter.Listener {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        return invoker.invoke(invocation);
    }

    @Override
    public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
        if (appResponse.hasException() && GenericService.class != invoker.getInterface()) {
            try {
                Throwable e = appResponse.getException();

                // directly throw if it's checked exception
                if (!(e instanceof RuntimeException) && (e instanceof Exception)) {
                    return;
                }
                // directly throw if the exception appears in the signature
                try {
                    Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                    Class<?>[] exceptionClasses = method.getExceptionTypes();
                    for (Class<?> exceptionClass : exceptionClasses) {
                        if (e.getClass().equals(exceptionClass)) {
                            return;
                        }
                    }
                } catch (NoSuchMethodException ignore) {
                    return;
                }

                // for the exception not found in method's signature, print ERROR message in server's log.
                log.error("Got unchecked and undeclared exception which called by {}. " +
                        "service: {}, method: {}, exception: {}: {}",
                    RpcContext.getContext().getRemoteHost(),
                    invoker.getInterface().getName(), invocation.getMethodName(), e.getClass().getName(), e.getMessage(),
                    e);
                // directly throw if exception class and interface class are in the same jar file.
                String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
                String exceptionFile = ReflectUtils.getCodeBase(e.getClass());
                if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
                    return;
                }
                // directly throw if it's JDK exception
                String className = e.getClass().getName();
                if (className.startsWith("java.") || className.startsWith("javax.")) {
                    return;
                }

                // 增加处理自定义异常
                if (e instanceof BizException) {
                    return;
                }

                // directly throw if it's dubbo exception
                if (e instanceof RpcException) {
                    return;
                }

                // otherwise, wrap with RuntimeException and throw back to the client
                appResponse.setException(new RuntimeException(StringUtils.toString(e)));
            } catch (Throwable e) {
                log.warn("Fail to ExceptionFilter when called by {}. " +
                        "service: {}, method: {}, exception: {}: {}",
                    RpcContext.getContext().getRemoteHost(),
                    invoker.getInterface().getName(), invocation.getMethodName(), e.getClass().getName(), e.getMessage(),
                    e);
            }
        }
    }

    @Override
    public void onError(Throwable e, Invoker<?> invoker, Invocation invocation) {
        log.error("Got unchecked and undeclared exception which called by {}. " +
                "service: {}, method: {}, exception: {}: {}",
            RpcContext.getContext().getRemoteHost(),
            invoker.getInterface().getName(), invocation.getMethodName(), e.getClass().getName(), e.getMessage(),
            e);
    }
}
