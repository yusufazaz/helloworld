package io.sample.hello.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.SnsClientBuilder;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.SqsClientBuilder;

@Configuration
@Slf4j
@Profile("aws")
public class AwsConfig {

    @Value("${application.aws.region}")
    private String region;
    
    @Value("${application.aws.local.endpoint:}")
    private String localEndpoint;
    
    @Bean
    public SnsClient snsLocalClient() {
    	SnsClientBuilder builder =  SnsClient.builder()
                .region(Region.of(region))
                .credentialsProvider(getCredentialsProvider());
    	if(!localEndpoint.isEmpty()) {
    		builder.endpointOverride(URI.create(localEndpoint) ) ;
    	}
        return builder.build();
    }
    
    @Bean
    public S3Client s3LocalClient() {
        S3ClientBuilder builder = S3Client.builder()
          .region(Region.of(region))
          .credentialsProvider(getCredentialsProvider());
          
    	if(!localEndpoint.isEmpty()) {
    		builder.endpointOverride(URI.create(localEndpoint) ) ;
    	}
        return builder.build();

    }
    
 	@Bean
	public SqsClient amazonSqs() {
		SqsClientBuilder builder = SqsClient.builder()
				.region(Region.of(region))
				.credentialsProvider(getCredentialsProvider());

    	if(!localEndpoint.isEmpty()) {
    		builder.endpointOverride(URI.create(localEndpoint) ) ;
    	}
        return builder.build();
		
	}
    
    
	private AwsCredentialsProviderChain getCredentialsProvider() {
		return AwsCredentialsProviderChain.of(DefaultCredentialsProvider.builder().build());
	}

    
}