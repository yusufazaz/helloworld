package io.sample.hello.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Data
@ConfigurationProperties(prefix = "rds")
@PropertySource(value = "classpath:rds.yml", factory = YamlPropertySourceFactory.class)
@Component
@Slf4j
public class DataSourceProperties {

    private List<RDSConfig> datasources = new ArrayList<>();

    @Getter
    @Setter
    public static class RDSConfig {
        private String alias;
        private String url;
        private String driver;
        private String database;
        private String username;
        private String password;
        private Boolean enabled;
    }

}

class YamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertiesPropertySource createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
        Properties propertiesFromYaml = loadYamlIntoProperties(resource);
        String sourceName = name != null ? name : resource.getResource().getFilename();
        return new PropertiesPropertySource(Objects.requireNonNull(sourceName), propertiesFromYaml);
    }

    private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException e) {
            // for ignoreResourceNotFound
            Throwable cause = e.getCause();
            if (cause instanceof FileNotFoundException)
                throw (FileNotFoundException) e.getCause();
            throw e;
        }
    }
}