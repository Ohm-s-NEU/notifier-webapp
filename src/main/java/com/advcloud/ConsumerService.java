package com.advcloud;



import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public final class ConsumerService {
    

    @KafkaListener(topics = "top", groupId = "group_id")
    public void consumeNew(String message) {
        System.out.println(message);
    }
    
}
