package com.grudus.examshelper.configuration.security;

import com.grudus.examshelper.configuration.authenticated.UserAuthenticationProvider;
import com.grudus.examshelper.configuration.authenticated.filters.CorsFilter;
import com.grudus.examshelper.configuration.authenticated.filters.StatelessAuthenticationFilter;
import com.grudus.examshelper.configuration.authenticated.filters.StatelessLoginFilter;
import com.grudus.examshelper.configuration.authenticated.stateless.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@Order(1)
public class StatelessSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationService tokenAuthenticationService;
    private final UserAuthenticationProvider userAuthenticationProvider;

    @Autowired
    public StatelessSecurityConfiguration(TokenAuthenticationService tokenAuthenticationService, UserAuthenticationProvider userAuthenticationProvider) {
        this.tokenAuthenticationService = tokenAuthenticationService;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new CorsFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new StatelessLoginFilter("/api/auth/login", tokenAuthenticationService, userAuthenticationProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService),
                        UsernamePasswordAuthenticationFilter.class)
        ;
    }
}
