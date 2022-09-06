package io.sample.hello.config;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import io.sample.hello.service.AWSResourceService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Profile("aws")
public class AWSResources {

    private Map<String, Object> awsResources;

    private final AWSResourceService awsResourceService;

    public AWSResources(AWSResourceService awsResourceService) {

        this.awsResourceService = awsResourceService;
    }


    @PostConstruct
    @Lazy
    public void initDatasources() {
    	log.info("Initializing AWS Resources");
    	awsResources = awsResourceService.loadAWSResources();
    }

}