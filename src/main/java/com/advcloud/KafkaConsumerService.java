package com.advcloud;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public final class KafkaConsumerService {
    

    @KafkaListener(topics = "top", groupId = "myGroup")
    public void consumeNew(String message) {
        System.out.println(message);
        System.out.println("Test Successful");
    }
    
}
