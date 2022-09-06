package io.sample.hello.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import io.sample.hello.config.AWSProperties;
import io.sample.hello.config.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>RDSDatasourceService</h2>
 * This class is used to store all information from rds databases.
 */
@Service
@Slf4j
@Profile("aws")
public class AWSResourceService {



    private final AWSProperties awsProperties;
    
    private final Map<String, Object> awsResourcesMap = new ConcurrentHashMap<>();


    public AWSResourceService(AWSProperties awsProperties) {
        this.awsProperties = awsProperties;
    }

     public Map<String, Object> loadAWSResources() {


            List<AWSProperties.AWSConfig> awsResources = awsProperties.getResources();
            awsResources.forEach(d -> {
            	if(d.getEnabled()) {
	                log.info("AWS Resource Config "+ d);
					/*
					 * try(Connection c = dataSource.getConnection()) {
					 * rdsDataSources.put(d.getAlias(), dataSource);
					 * log.info("Datasource {} connection succesfull", d.getAlias());
					 * }catch(Exception e) { log.warn("Unable to connect to RDS datasource {}",
					 * d.getAlias()); }
					 */
            	}else {
            		log.info("AWS Resource {} is disabled", d.getAlias());
            	}
            });
            return awsResourcesMap;
    }

 }
