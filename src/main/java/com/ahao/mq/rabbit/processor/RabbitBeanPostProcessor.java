package com.ahao.mq.rabbit.processor;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Rabbit 相关Bean的后处理器
 */
public class RabbitBeanPostProcessor implements BeanPostProcessor {

    private RabbitCollector messageProcessorCollector;
    public RabbitBeanPostProcessor() {
        this(new RabbitCollector());
    }
    public RabbitBeanPostProcessor(RabbitCollector messageProcessorCollector) {
        this.messageProcessorCollector = messageProcessorCollector;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof RabbitTemplate) {
            warpRabbitTemplate((RabbitTemplate) bean, beanName);
        }
        if (bean instanceof AbstractRabbitListenerContainerFactory) {
            warpContainerFactory((AbstractRabbitListenerContainerFactory) bean, beanName);
        }
        return bean;
    }

    private void warpContainerFactory(AbstractRabbitListenerContainerFactory bean, String beanName) {
        bean.setBeforeSendReplyPostProcessors(messageProcessorCollector.getFactoryAfterMessagePostProcessorArray());
        bean.setAfterReceivePostProcessors(messageProcessorCollector.getFactoryAfterMessagePostProcessorArray()); // 处理 @RabbitListener
    }

    private void warpRabbitTemplate(RabbitTemplate bean, String beanName) {
        bean.setBeforePublishPostProcessors(messageProcessorCollector.getTemplateBeforeMessagePostProcessorArray());
        bean.setAfterReceivePostProcessors(messageProcessorCollector.getTemplateAfterMessagePostProcessorArray());

        bean.setConfirmCallback(messageProcessorCollector.getConfirmCallback());
        bean.setReturnCallback(messageProcessorCollector.getReturnCallback());
        bean.setRecoveryCallback(messageProcessorCollector.getRecoveryCallback());
    }
}
