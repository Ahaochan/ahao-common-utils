package com.ahao.mq.rabbit.processor;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.CorrelationDataPostProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * 后处理收集器, 在 {@link RabbitBeanPostProcessor} 中统一设置
 */
public class RabbitCollector {
    private List<MessagePostProcessor> factoryBeforeMessagePostProcessorList;
    private List<MessagePostProcessor> factoryAfterMessagePostProcessorList;
    private CorrelationDataPostProcessor correlationDataPostProcessor;

    public RabbitCollector() {
        this.factoryBeforeMessagePostProcessorList = new ArrayList<>(16);
        this.factoryAfterMessagePostProcessorList = new ArrayList<>(16);
    }

    public interface Before extends MessagePostProcessor {
        // 标记处理器, 供 Collector 收集
    }

    public interface After extends MessagePostProcessor {
        // 标记处理器, 供 Collector 收集
    }

    public void setFactoryBeforeMessagePostProcessorList(List<MessagePostProcessor> factoryBeforeMessagePostProcessorList) {
        this.factoryBeforeMessagePostProcessorList = factoryBeforeMessagePostProcessorList;
    }

    public void setFactoryAfterMessagePostProcessorList(List<MessagePostProcessor> factoryAfterMessagePostProcessorList) {
        this.factoryAfterMessagePostProcessorList = factoryAfterMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getFactoryBeforeMessagePostProcessorList() {
        return factoryBeforeMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getFactoryAfterMessagePostProcessorList() {
        return factoryAfterMessagePostProcessorList;
    }

    public MessagePostProcessor[] getFactoryBeforeMessagePostProcessorArray() {
        return this.getFactoryBeforeMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getFactoryAfterMessagePostProcessorArray() {
        return this.getFactoryAfterMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public CorrelationDataPostProcessor getCorrelationDataPostProcessor() {
        return correlationDataPostProcessor;
    }

    public void setCorrelationDataPostProcessor(CorrelationDataPostProcessor correlationDataPostProcessor) {
        this.correlationDataPostProcessor = correlationDataPostProcessor;
    }
}
