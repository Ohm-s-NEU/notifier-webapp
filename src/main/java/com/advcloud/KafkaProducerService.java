package com.advcloud;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import com.advcloud.Model.Alert;

@Component
public class KafkaProducerService {

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;
	
	private static final String TOPIC = "notifier";


	public void sendMessage(Alert alert) {
		
		kafkaTemplate.send("notifier",alert.getId(),alert.getUserName());
	}

}
