package com.csen160.ecommercebackend.service;

import com.csen160.ecommercebackend.dto.UserRegisteredEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String USER_REGISTERED_TOPIC = "user-registered";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserRegisteredEvent(UserRegisteredEvent event) {
        try {
            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(USER_REGISTERED_TOPIC, event);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Sent user registered event with offset=[{}]", result.getRecordMetadata().offset());
                } else {
                    logger.error("Unable to send user registered event=[{}] due to : {}", event, ex.getMessage());
                }
            });
        } catch (Exception e) {
            logger.error("Error publishing user registered event", e);
        }
    }
}

