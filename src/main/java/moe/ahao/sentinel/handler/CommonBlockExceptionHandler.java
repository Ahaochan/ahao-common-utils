package moe.ahao.sentinel.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import moe.ahao.domain.entity.Result;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

public class CommonBlockExceptionHandler implements BlockExceptionHandler {
    @Setter
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        Result<Object> result = null;
        if (e instanceof FlowException) {
            result = Result.get(2002, "限流控制FlowException");
        } else if (e instanceof DegradeException) {
            result = Result.get(2003, "降级控制DegradeException");
        } else if (e instanceof AuthorityException) {
            result = Result.get(2004, "授权控制AuthorityException");
        } else if (e instanceof ParamFlowException) {
            result = Result.get(2005, "热点参数ParamFlowException");
        } else if(e instanceof SystemBlockException) {
            result = Result.get(2006, "系统规则SystemBlockException");
        }
        // 输出响应结果到前端
        response.setStatus(200);
        String json = objectMapper.writeValueAsString(result);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json);
    }
}
