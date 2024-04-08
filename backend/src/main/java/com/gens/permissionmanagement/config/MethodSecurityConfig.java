package com.gens.permissionmanagement.config;

import com.gens.common.constants.Constants;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.interceptor.SimpleTraceInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private Environment environment;

    public MethodInterceptor methodInterceptor(MethodSecurityMetadataSource methodSecurityMetadataSource) {
        boolean enableMethodLevelSecurity = Boolean.parseBoolean(environment.getProperty(Constants.METHOD_LEVEL_SECURITY, "true"));
        return enableMethodLevelSecurity ?
                super.methodSecurityInterceptor(methodSecurityMetadataSource) : new SimpleTraceInterceptor();
    }
}