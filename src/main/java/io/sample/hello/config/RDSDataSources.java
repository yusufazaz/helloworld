package io.sample.hello.config;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.sample.hello.service.RDSDatasourceService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RDSDataSources {

    private Map<String, DataSource> rdsDatasources;

    private final RDSDatasourceService rdsDatasourceService;

    public RDSDataSources(RDSDatasourceService rdsDatasourceService) {

        this.rdsDatasourceService = rdsDatasourceService;
    }


    @PostConstruct
    @Lazy
    public void initDatasources() {
    	log.info("Initializing RDS Datasources");
    	rdsDatasources = rdsDatasourceService.loadRDSDatasources();
    }

}