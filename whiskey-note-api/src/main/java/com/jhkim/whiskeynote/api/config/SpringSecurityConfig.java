package com.jhkim.whiskeynote.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jhkim.whiskeynote.api.exception.ExceptionHandlerFilter;
import com.jhkim.whiskeynote.api.jwt.JwtAuthorizationFilter;
import com.jhkim.whiskeynote.api.jwt.JwtUtils;
import com.jhkim.whiskeynote.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final ObjectMapper mapper;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable(); //basic Authentication filter 비활성
        http.csrf().disable();
        http.rememberMe().disable();

        //세션을 사용하지 않음
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(
            new ExceptionHandlerFilter(),
            UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(
                        userRepository,
                        jwtUtils
                ), BasicAuthenticationFilter.class
        );

        http.authorizeRequests()
                .antMatchers("/test").authenticated()
                .antMatchers("/test/admin").hasRole("ADMIN")
                .antMatchers("/test/user").hasRole("USER")
                .anyRequest().permitAll();
    }
}