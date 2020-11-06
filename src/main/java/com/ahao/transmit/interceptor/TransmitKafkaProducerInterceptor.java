package com.ahao.transmit.interceptor;

import com.ahao.transmit.properties.TransmitProperties;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Kafka 消息透传处理器
 * 因为 Kafka 是批量消息消费的, 所以需要在业务代码里添加透传出来, 不能使用 {@link org.apache.kafka.clients.consumer.ConsumerInterceptor}
 */
public class TransmitKafkaProducerInterceptor implements ProducerInterceptor<String, String> {
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

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // 该方法会在消息成功提交或发送失败之后被调用
        // onAcknowledgement 的调用要早于 callback 的调用
        // onAcknowledgement 和 onSend 不在同一个线程内
    }

    @Override
    public void configure(Map<String, ?> configs) {

    }

    @Override
    public void close() {

    }
}
