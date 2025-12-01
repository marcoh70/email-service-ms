package com.app.ed.emailservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.kafka.consumer")
public class KafkaConsumerProperties {

    private List<String> bootstrapServer;


}
