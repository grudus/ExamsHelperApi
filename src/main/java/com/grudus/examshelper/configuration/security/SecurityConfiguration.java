package com.grudus.examshelper.configuration.security;

import com.grudus.examshelper.configuration.authenticated.UserAuthenticationProvider;
import com.grudus.examshelper.configuration.authenticated.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Order(2)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationProvider userAuthenticationProvider;
    private final UserDetailsServiceImpl userDetailsService;


    @Autowired
    public SecurityConfiguration(UserAuthenticationProvider userAuthenticationProvider,
                                 UserDetailsServiceImpl userDetailsService) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/api/user/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .successHandler(new SimpleUrlAuthenticationSuccessHandler("/api/user"))
        .and().csrf().ignoringAntMatchers("**")
 ;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(userAuthenticationProvider)
                .userDetailsService(userDetailsService);
    }


}
