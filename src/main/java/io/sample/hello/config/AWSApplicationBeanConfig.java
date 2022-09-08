package io.sample.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


@Configuration
@Profile("aws")
public class AWSApplicationBeanConfig {

    @Autowired
    AWSResources awsResources;
    
    public AWSResources getAWSResources() {
    	return awsResources;
    }

}
