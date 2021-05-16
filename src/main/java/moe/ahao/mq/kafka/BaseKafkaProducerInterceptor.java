package moe.ahao.mq.kafka;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public abstract class BaseKafkaProducerInterceptor<K, V> implements ProducerInterceptor<K, V> {

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // 该方法会在消息成功提交或发送失败之后被调用
        // onAcknowledgement 的调用要早于 callback 的调用
        // onAcknowledgement 和 onSend 不在同一个线程内
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
