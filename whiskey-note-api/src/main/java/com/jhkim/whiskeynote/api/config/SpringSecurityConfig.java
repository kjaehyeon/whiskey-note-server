package com.jhkim.whiskeynote.api.config;

import com.jhkim.whiskeynote.api.jwt.JwtAuthenticationFilter;
import com.jhkim.whiskeynote.api.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().disable(); //basic Authentication filter 비활성
        http.csrf().disable();
        http.rememberMe().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(
                new JwtAuthenticationFilter(authenticationManager()),
                UsernamePasswordAuthenticationFilter.class
        ).addFilterBefore(
                new JwtAuthorizationFilter(authenticationManager()),
                BasicAuthenticationFilter.class
        );

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