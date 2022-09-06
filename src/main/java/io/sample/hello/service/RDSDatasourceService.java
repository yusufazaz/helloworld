package io.sample.hello.service;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import io.sample.hello.config.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>RDSDatasourceService</h2>
 * This class is used to store all information from rds databases.
 */
@Service
@Slf4j
public class RDSDatasourceService {


    private final JdbcTemplate jdbcTemplate;

    private final DataSourceProperties dataSourceProperties;
    
    private final Map<String, DataSource> rdsDataSources = new ConcurrentHashMap<>();


    public RDSDatasourceService(JdbcTemplate jdbcTemplate,  DataSourceProperties dataSourceProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSourceProperties = dataSourceProperties;
    }

     public Map<String, DataSource> loadRDSDatasources() {


            List<DataSourceProperties.RDSConfig> rdsSources = dataSourceProperties.getDatasources();
            rdsSources.forEach(d -> {
            	if(d.getEnabled()) {
	                DataSource dataSource = DataSourceBuilder.create()
	                        .driverClassName(d.getDriver())
	                        .url(d.getUrl())
	                        .username(d.getUsername())
	                        .password(d.getPassword())
	                        .build();
	                try(Connection c = dataSource.getConnection()) {
	                	rdsDataSources.put(d.getAlias(), dataSource);   
	                	log.info("Datasource {} connection succesfull", d.getAlias());
	                }catch(Exception e) {
	                	log.warn("Unable to connect to RDS datasource {}", d.getAlias());
	                }
            	}else {
            		log.info("Datasource {} is disabled", d.getAlias());
            	}
            });
            return rdsDataSources;
    }

 }
