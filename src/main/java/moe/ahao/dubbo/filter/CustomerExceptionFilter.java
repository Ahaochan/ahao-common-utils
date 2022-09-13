package moe.ahao.dubbo.filter;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.exception.BizException;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.service.GenericService;

@Slf4j
@Activate(group = CommonConstants.PROVIDER)
public class CustomerExceptionFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {
        try {
            // 1. 直接执行代码
            Result result = invoker.invoke(invocation);

            // 2. 进行异常处理
            if (result.hasException() && GenericService.class != invoker.getInterface()) {
                Throwable e = result.getException();
                log.error("Error {}. service: {}, method: {}, exception: {}: {}",
                    RpcContext.getContext().getRemoteHost(),
                    invoker.getInterface().getName(), invocation.getMethodName(), e.getClass().getName(),
                    e.getMessage(), e);
                return this.handleException(result);
            }
        } catch (RuntimeException e) {
            log.error("Error {}. service: {}, method: {}, exception: {}: {}",
                RpcContext.getContext().getRemoteHost(),
                invoker.getInterface().getName(), invocation.getMethodName(), e.getClass().getName(),
                e.getMessage(), e);
        }
        return null;
    }

    private Result handleException(Result result) {
        Throwable exception = result.getException();
        moe.ahao.domain.entity.Result<?> jsonResult;
        if (exception instanceof BizException) {
            BizException bizException = (BizException) exception;
            jsonResult = moe.ahao.domain.entity.Result.failure(bizException.getMessage());
        } else {
            jsonResult = moe.ahao.domain.entity.Result.failure(exception.getMessage());
        }
        result.setValue(jsonResult);
        result.setException(null);
        return result;
    }
}
