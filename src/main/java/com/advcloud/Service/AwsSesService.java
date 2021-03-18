package com.advcloud.Service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AwsSesService {

    private static final String CHAR_SET = "UTF-8";
    private static final String SUBJECT = "Test from our application";
    private final AmazonSimpleEmailService emailService;
    private final String sender;
    
    @Autowired
    public AwsSesService(AmazonSimpleEmailService emailService,
                         @Value("${email.sender}") String sender) {
        this.emailService = emailService;
        this.sender = sender;
    }

    /**
     * This method send email using the aws ses sdk
     * @param email email
     * @param body body
     */
    public void sendEmail(String email, String body) {
        try {
            // The time for request/response round trip to aws in milliseconds
        	
        	AWSCredentials credentials = new BasicAWSCredentials("AKIAUWJTDERIVYFIUW7C", "TDmEiLBHdlXH1sYkY8NvWC3F+ketq/svRdFFCpXp");
    		com.amazonaws.services.simpleemail.AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
    				.standard().withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("us-east-1").build();
    		
            int requestTimeout = 3000;
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(email))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withText(new Content()
                                            .withCharset(CHAR_SET).withData(body)))
                            .withSubject(new Content()
                                    .withCharset(CHAR_SET).withData(SUBJECT)))
                    .withSource(sender).withSdkRequestTimeout(requestTimeout);
            emailService.sendEmail(request);
        } catch (RuntimeException e) {
        		System.out.println(e);
        }
    }
}
