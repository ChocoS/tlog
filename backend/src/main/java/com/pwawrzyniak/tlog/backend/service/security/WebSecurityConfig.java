package com.pwawrzyniak.tlog.backend.service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .authorizeRequests().antMatchers("/").permitAll().antMatchers("/main").authenticated()
        // start - enable h2-console
        .and().authorizeRequests().antMatchers("/h2-console/**").permitAll()
        .and().headers().frameOptions().sameOrigin()
        // end - enable h2-console
        .and().formLogin().loginPage("/login").defaultSuccessUrl("/main", true).failureUrl("/login?error=true")
        .and().logout().logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID");
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}