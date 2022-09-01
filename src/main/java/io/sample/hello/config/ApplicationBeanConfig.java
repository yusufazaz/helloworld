package io.sample.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationBeanConfig {

    @Autowired
    RDSDataSources rdsDataSources;
    
    public RDSDataSources getRDSDatasources() {
    	return rdsDataSources;
    }

}
