package com.app.ed.emailservice.handler;

import com.app.ed.core.model.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    @KafkaHandler
    public void handler(ProductCreatedEvent productCreatedEvent) {
      log.info("Consuming message {}", productCreatedEvent.getTitle());
    }
}
