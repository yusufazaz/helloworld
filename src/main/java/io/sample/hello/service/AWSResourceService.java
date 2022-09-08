package io.sample.hello.service;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import io.sample.hello.config.AWSProperties;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


/**
 * <h2>RDSDatasourceService</h2> This class is used to store all information
 * from rds databases.
 */
@Service
@Slf4j
@Profile("aws")
public class AWSResourceService {

	private final AWSProperties awsProperties;

	private final Map<String, Object> awsResourcesMap = new ConcurrentHashMap<>();

	private final SqsClient sqsClient;

	private final SnsClient snsClient;

	private final S3Client s3Client;

	public AWSResourceService(AWSProperties awsProperties, SqsClient sqsClient, SnsClient snsClient,
			S3Client s3Client) {
		this.awsProperties = awsProperties;
		this.sqsClient = sqsClient;
		this.snsClient = snsClient;
		this.s3Client = s3Client;
	}

	public Map<String, Object> loadAWSResources() {

		List<AWSProperties.AWSConfig> awsResources = awsProperties.getAwsResources();
		log.debug("awsResources = {}", awsResources);
		awsResources.forEach(d -> {
			if (d.getEnabled()) {
				log.info("AWS Resource Config " + d);
				if(d.getEnabled()) {
					awsResourcesMap.put(d.getAlias(), d);
					if(d.getType().equals("SQS")) {
						sendAndRecieveSQSMessage(d.getName());
					}
				}

			} else {
				log.info("AWS Resource {} is disabled", d.getAlias());
			}
		});
		return awsResourcesMap;
	}
	
	public void sendAndRecieveSQSMessage(String queueURL) {
		String messageBody = "message-body-"+UUID.randomUUID().toString();
		SendMessageRequest message = SendMessageRequest.builder().queueUrl(queueURL).messageBody(messageBody).build();
		try{
			sqsClient.sendMessage(message);
		} catch (Exception e) {
            log.warn("Failed to send message on SQS queue {}",queueURL );
        }
        List<Message> messages = sqsClient.receiveMessage(
                ReceiveMessageRequest.builder().queueUrl(queueURL).waitTimeSeconds(20).build())
                .messages();
         
         if(messageBody.equals(messages.get(0).body())) {
        	 log.info("message sent was recieved");
         }else {
        	 log.warn("message that was sent not  recieved");
         }
	}

}
