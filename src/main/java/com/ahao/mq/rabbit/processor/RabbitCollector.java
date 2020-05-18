package com.ahao.mq.rabbit.processor;

import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.RecoveryCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * 后处理收集器, 在 {@link RabbitBeanPostProcessor} 中统一设置
 */
public class RabbitCollector {
    private List<MessagePostProcessor> templateBeforeMessagePostProcessorList;
    private List<MessagePostProcessor> templateAfterMessagePostProcessorList;
    private List<MessagePostProcessor> factoryBeforeMessagePostProcessorList;
    private List<MessagePostProcessor> factoryAfterMessagePostProcessorList;
    private RabbitTemplate.ConfirmCallback confirmCallback;
    private RabbitTemplate.ReturnCallback returnCallback;
    private RecoveryCallback<?> recoveryCallback;

    public RabbitCollector() {
        this.templateBeforeMessagePostProcessorList = new ArrayList<>(16);
        this.templateAfterMessagePostProcessorList = new ArrayList<>(16);
        this.factoryBeforeMessagePostProcessorList = new ArrayList<>(16);
        this.factoryAfterMessagePostProcessorList = new ArrayList<>(16);
    }

    public interface Before extends MessagePostProcessor {
        // 标记处理器, 供 Collector 收集
    }

    public interface After extends MessagePostProcessor {
        // 标记处理器, 供 Collector 收集
    }


    public void setTemplateBeforeMessagePostProcessorList(List<MessagePostProcessor> templateBeforeMessagePostProcessorList) {
        this.templateBeforeMessagePostProcessorList = templateBeforeMessagePostProcessorList;
    }

    public void setTemplateAfterMessagePostProcessorList(List<MessagePostProcessor> templateAfterMessagePostProcessorList) {
        this.templateAfterMessagePostProcessorList = templateAfterMessagePostProcessorList;
    }

    public void setFactoryBeforeMessagePostProcessorList(List<MessagePostProcessor> factoryBeforeMessagePostProcessorList) {
        this.factoryBeforeMessagePostProcessorList = factoryBeforeMessagePostProcessorList;
    }

    public void setFactoryAfterMessagePostProcessorList(List<MessagePostProcessor> factoryAfterMessagePostProcessorList) {
        this.factoryAfterMessagePostProcessorList = factoryAfterMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getTemplateBeforeMessagePostProcessorList() {
        return templateBeforeMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getTemplateAfterMessagePostProcessorList() {
        return templateAfterMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getFactoryBeforeMessagePostProcessorList() {
        return factoryBeforeMessagePostProcessorList;
    }

    public List<MessagePostProcessor> getFactoryAfterMessagePostProcessorList() {
        return factoryAfterMessagePostProcessorList;
    }

    public MessagePostProcessor[] getTemplateBeforeMessagePostProcessorArray() {
        return this.getTemplateBeforeMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getTemplateAfterMessagePostProcessorArray() {
        return this.getTemplateAfterMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getFactoryBeforeMessagePostProcessorArray() {
        return this.getFactoryBeforeMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public MessagePostProcessor[] getFactoryAfterMessagePostProcessorArray() {
        return this.getFactoryAfterMessagePostProcessorList().toArray(new MessagePostProcessor[0]);
    }

    public RabbitTemplate.ConfirmCallback getConfirmCallback() {
        return confirmCallback;
    }

    public void setConfirmCallback(RabbitTemplate.ConfirmCallback confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public RabbitTemplate.ReturnCallback getReturnCallback() {
        return returnCallback;
    }

    public void setReturnCallback(RabbitTemplate.ReturnCallback returnCallback) {
        this.returnCallback = returnCallback;
    }

    public RecoveryCallback<?> getRecoveryCallback() {
        return recoveryCallback;
    }

    public void setRecoveryCallback(RecoveryCallback<?> recoveryCallback) {
        this.recoveryCallback = recoveryCallback;
    }
}
