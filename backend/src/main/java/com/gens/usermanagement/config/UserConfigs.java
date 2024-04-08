package com.gens.usermanagement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "couchbase-doc-maintainer.user-management.user-resource")
public class UserConfigs {

    private String classIdValue;

    private String initialValue;
}