package io.sample.hello.config;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.sample.hello.service.RDSDatasourceService;
import io.sample.hello.service.Result;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RDSDataSources {

    private Map<String, Result> rdsDatasourcesResults;

    private final RDSDatasourceService rdsDatasourceService;

    public RDSDataSources(RDSDatasourceService rdsDatasourceService) {
        this.rdsDatasourceService = rdsDatasourceService;
    }


    @PostConstruct
    @Lazy
    public void initDatasources() {    	
    	rdsDatasourcesResults = rdsDatasourceService.loadRDSDatasources();
    	log.info("Initializing RDS Datasources={}",rdsDatasourcesResults);
    }
    
    public Map<String, Result>  getDatasourceResults(){
    	return rdsDatasourcesResults;
    }

}