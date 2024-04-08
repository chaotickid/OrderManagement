package com.gens.common.configs;

import com.gens.common.exceptionHandling.CustomAccessDeniedHandler;
import com.gens.common.exceptionHandling.Http401AuthenticationEntryPoint;
import com.gens.common.utils.JwtTokenProvider;
import com.gens.usermanagement.service.CustomUserDetailsService;
import com.gens.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author : Aditya Patil
 * @Date : 24-06-2023
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private GlobalTokenDecoder globalTokenDecoder;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private Environment environment;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    //cors configurations
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers()
                .contentSecurityPolicy("default-src 'self' localhost:*;");

        http.authorizeHttpRequests()
                .antMatchers("/v2/api-docs/**",
                        "/v3/api-docs/**",
                        "/api/usermanagement/signup/**",
                        "/api/usermanagement/signin/**")
                .permitAll().anyRequest().authenticated().and().requestCache()
                .requestCache(new NullRequestCache()).and()
                .addFilter(new CommonAuthorizationFilter(authenticationManager(), jwtTokenProvider, userService, globalTokenDecoder, environment))
                .exceptionHandling()
                .authenticationEntryPoint(new Http401AuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .cors().and().csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}