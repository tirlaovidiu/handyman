package com.qubiz.server.config;

import com.qubiz.server.service.UserRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRegister userRegister;

    @Autowired
    public WebSecurityConfig(UserRegister userRegister) {
        this.userRegister = userRegister;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/login**", "/login/google/token", "/error**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(ssoFilters(), BasicAuthenticationFilter.class)
                .exceptionHandling()
                .and().logout().logoutUrl("/logout");
    }

    private Filter ssoFilters() {
        CompositeFilter filter = new CompositeFilter();
        List<Filter> filters = new ArrayList<>();
        filters.add(new LoginFilter(google(), "/login/google/token", userRegister));
        filter.setFilters(filters);
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

}
