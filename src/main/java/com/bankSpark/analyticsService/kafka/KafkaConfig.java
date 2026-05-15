package com.bankSpark.analyticsService.kafka;

import com.bankSpark.analyticsService.DTO.UserDTO;
import com.bankSpark.analyticsService.DTO.anomaly.KafkaAnomalyDTO;
import com.bankSpark.analyticsService.DTO.segmentsRFM.KafkaSegmentUserDTO;
import com.bankSpark.analyticsService.ORM.User;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private Map<String,Object> basicConfig(){
        Map<String,Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka_producer:9092");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,1);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "analytics-service-group-v2");
        return config;
    }

    //Контейнеры User

    @Bean
    public ConsumerFactory<String, UserDTO> userConsumerFactory(){
        Map<String, Object> props = basicConfig();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,UserDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserDTO> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, UserDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        factory.setConcurrency(1);
        return factory;
    }

    //Контейнеры User

    //Контейнеры SegmentUser

    @Bean
    public ConsumerFactory<String, KafkaSegmentUserDTO> segmentUserConsumerFactory(){
        Map<String, Object> props = basicConfig();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,KafkaSegmentUserDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaSegmentUserDTO> segmentUserKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, KafkaSegmentUserDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(segmentUserConsumerFactory());
        factory.setConcurrency(1);
        return factory;
    }

    //Контейнеры SegmentUser

    //Контейнеры Anomaly

    @Bean
    public ConsumerFactory<String, KafkaAnomalyDTO> anomalyConsumerFactory(){
        Map<String, Object> props = basicConfig();
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,KafkaAnomalyDTO.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, KafkaAnomalyDTO> anomalyKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, KafkaAnomalyDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(anomalyConsumerFactory());
        factory.setConcurrency(1);
        return factory;
    }

    //Контейнеры Anomaly

}