package com.qubiz.server.config;

import com.qubiz.server.service.UserRegister;
import com.qubiz.server.util.security.OAuth2ClientAuthenticationProcessingAndSavingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 14.06.2018 #
 ******************************
*/
@Configuration
@EnableOAuth2Client
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2ClientContext oauth2ClientContext;
    private final UserRegister userRegister;

    @Autowired
    public WebSecurityConfig(OAuth2ClientContext oauth2ClientContext, UserRegister userRegister) {
        this.oauth2ClientContext = oauth2ClientContext;
        this.userRegister = userRegister;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/login/**", "/error**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(ssoFilters(), BasicAuthenticationFilter.class)
                .exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());

    }

    private Filter ssoFilters() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(ssoFilter(google(), googleResource(), "/login/google"));
        filters.add(new CustomFilter(google(), "/login/google/token", userRegister));
        filter.setFilters(filters);
        return filter;
    }

    private Filter ssoFilter(AuthorizationCodeResourceDetails client, ResourceServerProperties resourceServerProperties, String path) {

        OAuth2ClientAuthenticationProcessingAndSavingFilter filter = new OAuth2ClientAuthenticationProcessingAndSavingFilter(path);
        OAuth2RestTemplate template = new OAuth2RestTemplate(client, oauth2ClientContext);
        filter.setRestTemplate(template);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(
                resourceServerProperties.getUserInfoUri(), client.getClientId());
        tokenServices.setRestTemplate(template);
        filter.setTokenServices(tokenServices);
        return filter;
    }

    @Bean
    @ConfigurationProperties("google.client")
    public AuthorizationCodeResourceDetails google() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    @ConfigurationProperties("google.resource")
    public ResourceServerProperties googleResource() {
        return new ResourceServerProperties();
    }

    @Bean
    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(filter);
        registration.setOrder(-100);
        return registration;
    }

}
