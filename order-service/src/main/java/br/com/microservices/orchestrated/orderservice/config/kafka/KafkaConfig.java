package br.com.microservices.orchestrated.orderservice.config.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

import lombok.RequiredArgsConstructor;

/**
 * @author pedroRhamon
 */
@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
	
	@Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.topic.start-saga}")
    private String startSagaTopic;

    @Value("${spring.kafka.topic.notify-ending}")
    private String notifyEndingTopic;

}
