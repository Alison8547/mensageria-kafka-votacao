package com.api.kafkavotacao.infrastructure.configuration;

import com.api.kafkavotacao.domain.model.ResultPayload;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.client-id}")
    private String clientId;

    @Value("${spring.kafka.template.default-topic}")
    private String topic;


    @Bean
    public Map<String, Object> producerConfiguration() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "1000");
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, "60000");
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, "5000");
        configProps.put(ProducerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, "60000");
        configProps.put(ProducerConfig.RECONNECT_BACKOFF_MS_CONFIG, "30000");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return configProps;
    }

    @Bean
    public ProducerFactory<String, ResultPayload> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfiguration());
    }

    @Bean
    public KafkaTemplate<String, ResultPayload> kafkaTemplate() {
        KafkaTemplate<String, ResultPayload> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setDefaultTopic(topic);
        return kafkaTemplate;
    }
}
