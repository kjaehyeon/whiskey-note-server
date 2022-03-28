package com.jhkim.whiskeynote.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable();
        http.csrf().disable();

        http.authorizeRequests()
                .anyRequest().permitAll();

        http.formLogin()
                .loginProcessingUrl("/api/auth/sign-in")
                .permitAll();

        http.logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/auth/sign-out"))
                .invalidateHttpSession(true)
                .deleteCookies();
    }
}