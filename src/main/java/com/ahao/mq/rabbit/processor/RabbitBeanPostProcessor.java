package com.ahao.mq.rabbit.processor;

import org.springframework.amqp.rabbit.config.AbstractRabbitListenerContainerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Rabbit 相关Bean的后处理器
 */
public class RabbitBeanPostProcessor implements BeanPostProcessor {

    private RabbitCollector rabbitCollector;
    public RabbitBeanPostProcessor() {
        this(new RabbitCollector());
    }
    public RabbitBeanPostProcessor(RabbitCollector rabbitCollector) {
        this.rabbitCollector = rabbitCollector;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractRabbitListenerContainerFactory) {
            warpContainerFactory((AbstractRabbitListenerContainerFactory) bean, beanName);
        }
        return bean;
    }

    private void warpContainerFactory(AbstractRabbitListenerContainerFactory bean, String beanName) {
        bean.setBeforeSendReplyPostProcessors(rabbitCollector.getFactoryAfterMessagePostProcessorArray());
        bean.setAfterReceivePostProcessors(rabbitCollector.getFactoryAfterMessagePostProcessorArray()); // 处理 @RabbitListener
    }
}
