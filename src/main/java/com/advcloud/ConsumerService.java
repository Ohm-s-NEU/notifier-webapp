package com.advcloud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public final class ConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @KafkaListener(topics = "notifier", groupId = "group_id")
    public void consumeNew(String message) {
        logger.info(String.format("$$$$ => Consumed message: %s", message));
    }
    
}
