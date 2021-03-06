package com.advcloud.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

@org.springframework.stereotype.Service
public class SESService {

	@Value("${accessKey}")
	private String accessKey;

	@Value("${secretKey}")
	private String secretKey;

	@Value("${region}")
	private String region;
	
	@Autowired
    MeterRegistry registry;
	
	Timer sesTimer;

	public String from = "no_reply@prod.pavan.website";
	//public String[] to = {"srkantarao.p@northeastern.edu"};
	private String templateName = "MyTemplate";
	//private String templateData = "{ \"name\":\"Jack\", \"favoriteanimal\": \"Tiger\"}";
	
	private static final Logger logger = LoggerFactory.getLogger(SESService.class);

	public boolean sendEmail(JSONObject data, String userName) {

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		com.amazonaws.services.simpleemail.AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(region).build();

		Destination destination = new Destination();
		List<String> toAddresses = new ArrayList<String>();
		String[] Emails = {userName};

		for (String email : Emails) {
			toAddresses.add(email);
		}

		destination.setToAddresses(toAddresses);
		SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
		templatedEmailRequest.withDestination(destination);
		templatedEmailRequest.withTemplate(templateName);
		templatedEmailRequest.withTemplateData(data.toString());
		templatedEmailRequest.withSource(from);
		sesTimer = registry.timer("custom.metrics.timer", "SES", "Send_EMail_through_SES");
		sesTimer.record(()->client.sendTemplatedEmail(templatedEmailRequest));
		logger.info("Mail sent through SES");
		return true;
	}

}