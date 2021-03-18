package com.advcloud.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;

@org.springframework.stereotype.Service
public class Service {

	//@Value("${accessKey}")
	//private String accessKey;

	//@Value("${secretKey}")
	//private String secretKey;

	//@Value("${region}")
	//private String region;

	public String from = "no_reply@prod.pavan.website";
	public String[] to = {"sekar.h@northeastern.edu"};
	private String templateName = "MyTemplate";
	private String templateData = "{ \"name\":\"Jack\", \"favoriteanimal\": \"Tiger\"}";

	public String sendEmail(Map<String, Object> data, String userName) {

		AWSCredentials credentials = new BasicAWSCredentials("AKIAUWJTDERIVYFIUW7C", "TDmEiLBHdlXH1sYkY8NvWC3F+ketq/svRdFFCpXp");
		com.amazonaws.services.simpleemail.AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("us-east-1").build();

		Destination destination = new Destination();
		List<String> toAddresses = new ArrayList<String>();
		String[] Emails = to;

		for (String email : Emails) {
			toAddresses.add(email);
		}

		destination.setToAddresses(toAddresses);
		SendTemplatedEmailRequest templatedEmailRequest = new SendTemplatedEmailRequest();
		templatedEmailRequest.withDestination(destination);
		templatedEmailRequest.withTemplate(templateName);
		templatedEmailRequest.withTemplateData(templateData);
		templatedEmailRequest.withSource(from);
		client.sendTemplatedEmail(templatedEmailRequest);
		return "email sent";
	}

}