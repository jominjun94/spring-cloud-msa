package com.example.userservice.security;

import com.example.userservice.service.UserService;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.management.MXBean;
import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    private Environment env;
    private String IP = "192.168.168.116";
    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public WebSecurity( UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,  Environment env) {

        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/actuator/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/**")
                .access("hasIpAddress('"+IP+"')")                        .and()
                .addFilter(getAuthenticationFilter());

        http.headers().frameOptions().disable(); // h2 보이게 해줍니다.
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(),userService,env);
        authenticationFilter.setAuthenticationManager(authenticationManager());
        return authenticationFilter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);




    }
}
