package moe.ahao.spring.security.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;

/**
 * 使用 TransmittableThreadLocal 作为 SecurityContext 的存储媒介
 * @see org.springframework.security.core.context.SecurityContextHolder
 */
public class TtlSecurityContextHolderStrategy implements SecurityContextHolderStrategy {
    // ~ Static fields/initializers
    // =====================================================================================

    private static final ThreadLocal<SecurityContext> contextHolder = new TransmittableThreadLocal<>();

    // ~ Methods
    // ========================================================================================================

    public void clearContext() {
        contextHolder.remove();
    }

    public SecurityContext getContext() {
        SecurityContext ctx = contextHolder.get();

        if (ctx == null) {
            ctx = createEmptyContext();
            contextHolder.set(ctx);
        }

        return ctx;
    }

    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        contextHolder.set(context);
    }

    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
