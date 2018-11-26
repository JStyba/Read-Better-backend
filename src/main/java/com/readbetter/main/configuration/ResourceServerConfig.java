package com.readbetter.main.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@EnableResourceServer
@CrossOrigin(value = "*", maxAge = 3600)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
                anonymous().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/users/**","/entry/**","/**").authenticated()
                .antMatchers(HttpMethod.POST, "/users/**","/entry/**","/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/users/**","/entry/**","/**").authenticated()
                .antMatchers(HttpMethod.GET, "/admin/**").access("hasRole('ADMIN')")
                .antMatchers(HttpMethod.POST, "/admin/**").access("hasRole('ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/admin/**").access("hasRole('ADMIN')")
                .antMatchers("/users/register").access("hasRole('ADMIN')")
                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
//        anonymous().disable()
//               .requestMatchers().antMatchers("/entry/**","/scrape/")
//
//
//                .and().authorizeRequests().antMatchers("/users/**","/entry/**").permitAll()
//                .and().exceptionHandling().accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

}
