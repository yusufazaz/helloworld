package io.sample.hello.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class ApplicationBeanConfig {

	public final static String rdsCacheKey = "RDS";

    @Autowired
    RDSDataSources rdsDataSources;
    
    public RDSDataSources getRDSDatasources() {
    	return rdsDataSources;
    }

 }
