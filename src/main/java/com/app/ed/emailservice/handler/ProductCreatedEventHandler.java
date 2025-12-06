package com.app.ed.emailservice.handler;

import com.app.ed.core.model.ProductCreatedEvent;
import com.app.ed.emailservice.exceptions.NonRetriableMessageException;
import com.app.ed.emailservice.exceptions.RetriableMessageException;
import com.app.ed.emailservice.io.ProcessedEventEntity;
import com.app.ed.emailservice.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
@KafkaListener(topics = "product-created-events-topic")
public class ProductCreatedEventHandler {

    private final RestTemplate restTemplate;
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    @KafkaHandler
    public void handler(@Payload ProductCreatedEvent productCreatedEvent,
                        @Header("messageId") String messageId) {
        log.info("Payload received: {} - MessageId: {}", productCreatedEvent, messageId);
        ProcessedEventEntity existingEvent = processedEventRepository.findByMessageId(messageId);
        if (existingEvent != null){
            log.warn("The message with id {} is already processed", messageId);
            return;
        }

        String requestUrl = "http://localhost:8083/response/200";
        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if (response.getStatusCode().value() == HttpStatus.OK.value()) {
                log.info("Consuming message with restemplate {}", response.getBody());
            }
        } catch (ResourceAccessException ex) {
            log.error(ex.getMessage());
            throw new RetriableMessageException("Throwing retriable exception");
        } catch (HttpServerErrorException ex) {
            log.error(ex.getMessage());
            throw new NonRetriableMessageException("Error consuming resttemplate source");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RetriableMessageException("Exception when consuming resource with restTemplate");
        }
        try {
            processedEventRepository.save(new ProcessedEventEntity(messageId, productCreatedEvent.getId()));
        } catch (DataIntegrityViolationException exception) {
            throw new NonRetriableMessageException(exception);
        }
    }
}
