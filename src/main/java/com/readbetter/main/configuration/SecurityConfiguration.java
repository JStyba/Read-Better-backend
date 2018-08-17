//package com.readbetter.main.configuration;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.access.channel.ChannelProcessingFilter;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//
//    @Bean
//    public BCryptPasswordEncoder bCryptPasswordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/authenticate", "/user/register");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.cors().and().csrf().disable()
//
//                .authorizeRequests()
//                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
//                .antMatchers(HttpMethod.POST, "/user/register").permitAll()
//                .anyRequest().fullyAuthenticated().and()
//                .addFilterBefore(new JWTFilter(), UsernamePasswordAuthenticationFilter.class)
//                .httpBasic().and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//    }
//}
