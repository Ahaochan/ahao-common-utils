package moe.ahao.embedded;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.kafka.test.EmbeddedKafkaBroker;

import java.util.HashMap;
import java.util.Map;

public class EmbeddedKafkaTest {
    public static final String HOST = "127.0.0.1:9092";
    public static final boolean AUTO_COMMIT = false;

    public static final String TOPIC_NAME = "ahao-topic";
    public static final String GROUP_NAME = "ahao-group";

    public static final int SIZE = 100;
    public static final int PARTITION = 2;

    protected EmbeddedKafkaBroker broker;
    protected AdminClient adminClient;

    @BeforeEach
    public void beforeEach() {
        broker = new EmbeddedKafkaBroker(1).kafkaPorts(9092);
        broker.afterPropertiesSet();

        Map<String, Object> adminProperties = new HashMap<>();
        adminProperties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        adminClient = AdminClient.create(adminProperties);

        // 1. 生产消息
        Map<String, Object> producerProperties = EmbeddedKafkaTest.initProducerProperties();
        // 2. 指定分区器
        producerProperties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, SimplePartitioner.class.getName());
        try (Producer<String, String> producer = new KafkaProducer<>(producerProperties);) {
            for (int i = 0; i < SIZE; i++) {
                String partitionKey = "key" + i;
                String value = "value" + i;

                ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, partitionKey, value);
                producer.send(record, (recordMetadata, e) -> {
                    if (e != null) {
                        System.out.println("==================>" + "消息" + value + "发送失败");
                        e.printStackTrace();
                    } else {
                        System.out.println("==================>" + "消息" + value + "发送成功, offset:" + recordMetadata.offset() + ", partition:" + recordMetadata.partition());
                    }
                });
            }
        }
    }

    @AfterEach
    public void afterAll() throws Exception {
        if (adminClient != null) adminClient.close();
        if (broker != null) broker.destroy();
    }

    protected static Map<String, Object> initProducerProperties() {
        Map<String, Object> prop = new HashMap<>();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        prop.put(ProducerConfig.ACKS_CONFIG, "all");
        prop.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(0));
        prop.put(ProducerConfig.BATCH_SIZE_CONFIG, String.valueOf(16384));
        prop.put(ProducerConfig.LINGER_MS_CONFIG, String.valueOf(1));
        prop.put(ProducerConfig.BUFFER_MEMORY_CONFIG, String.valueOf(33554432));
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return prop;
    }

    protected static Map<String, Object> initConsumerProperties() {
        Map<String, Object> prop = new HashMap<>();
        prop.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, HOST);
        prop.put(ConsumerConfig.GROUP_ID_CONFIG, "group");
        prop.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(AUTO_COMMIT));
        prop.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(100));
        prop.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, String.valueOf(15000));
        prop.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        prop.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return prop;
    }

    public static class SimplePartitioner implements Partitioner {
        @Override
        public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
            String keyStr = String.valueOf(key);
            int index = Integer.parseInt(keyStr.substring(3)) % PARTITION;
            System.out.println("==================>" + "[" + value + "]消息投递到第[" + index + "]个partition");
            return index;
        }

        @Override
        public void close() {
        }

        @Override
        public void configure(Map<String, ?> configs) {
        }
    }
}
