package com.advcloud.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;

@org.springframework.stereotype.Service
public class Service {

	@Value("${accessKey}")
	private String accessKey;

	@Value("${secretKey}")
	private String secretKey;

	@Value("${region}")
	private String region;

	public String from = "no_reply@prod.pavan.website";
	//public String[] to = {"srkantarao.p@northeastern.edu"};
	private String templateName = "MyTemplate";
	//private String templateData = "{ \"name\":\"Jack\", \"favoriteanimal\": \"Tiger\"}";

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
		client.sendTemplatedEmail(templatedEmailRequest);
		return true;
	}

}