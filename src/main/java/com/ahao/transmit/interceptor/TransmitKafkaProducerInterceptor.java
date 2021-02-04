package com.ahao.transmit.interceptor;

import com.ahao.mq.kafka.BaseKafkaProducerInterceptor;
import com.ahao.transmit.properties.TransmitProperties;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;

/**
 * Kafka 消息透传处理器
 * 因为 Kafka 是批量消息消费的, 所以需要在业务代码里添加透传出来, 不能使用 {@link org.apache.kafka.clients.consumer.ConsumerInterceptor}
 */
public class TransmitKafkaProducerInterceptor extends BaseKafkaProducerInterceptor<String, String> {
    private TransmitProperties transmitProperties;
    public TransmitKafkaProducerInterceptor(TransmitProperties transmitProperties) {
        this.transmitProperties = transmitProperties;
    }

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord record) {
        // 发送前调用
        transmitProperties.apply((k, v) -> record.headers().add(k, v.getBytes(StandardCharsets.UTF_8)));
        return record;
    }
}
