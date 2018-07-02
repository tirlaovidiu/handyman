package com.qubiz.server.config;

import com.qubiz.server.service.AuthenticationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

    private final AuthenticationUserService authenticationUserService;

    @Autowired
    public WebSecurityConfig(AuthenticationUserService authenticationUserService) {
        this.authenticationUserService = authenticationUserService;
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
        filters.add(new LoginFilter("/login/google/token", authenticationUserService));
        filter.setFilters(filters);
        return filter;
    }

}
