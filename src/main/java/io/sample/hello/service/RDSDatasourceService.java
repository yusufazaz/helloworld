package io.sample.hello.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import io.sample.hello.config.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * <h2>RDSDatasourceService</h2> This class is used to store all information
 * from rds databases.
 */
@Service
@Slf4j
public class RDSDatasourceService {


	private final DataSourceProperties dataSourceProperties;

	private final Map<String, DataSource> rdsDataSources = new ConcurrentHashMap<>();

	public RDSDatasourceService( DataSourceProperties dataSourceProperties) {
		this.dataSourceProperties = dataSourceProperties;
	}

	@Cacheable("datasources")
	public Map<String, Result> loadRDSDatasources() {
		Map<String, Result> resultMap = new HashMap<String, Result>();
		List<DataSourceProperties.RDSConfig> rdsSources = dataSourceProperties.getDatasources();
		rdsSources.forEach(d -> {
			if (d.getEnabled()) {
				DataSource dataSource = DataSourceBuilder.create().driverClassName(d.getDriver()).url(d.getUrl())
						.username(d.getUsername()).password(d.getPassword()).build();
				try (Connection c = dataSource.getConnection()) {
					rdsDataSources.put(d.getAlias(), dataSource);
					log.info("Datasource {} connection succesfull", d.getAlias());
					Result result = new Result(d.getAlias(), true, null);
					resultMap.put(d.getAlias(), result);
				} catch (Exception e) {
					log.warn("Unable to connect to RDS datasource {}", d.getAlias());
					Result result = new Result(d.getAlias(), false, getStacktraceAsString(e));
					resultMap.put(d.getAlias(), result);

				}
			} else {
				log.info("Datasource {} is disabled", d.getAlias());
			}
		});
		log.debug("Results Map = {}", resultMap);
		return resultMap;
	}

	private String getStacktraceAsString(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}
	
	@CacheEvict(value="rds",allEntries=true)
	public void invalidateCache() {
		log.info("Invalidating RDS Cache");
	}
}
