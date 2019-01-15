package io.agileintelligence.ppmtool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    // Basic configuration of HTTP security
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and() // when exception happens because user is not authenticated then authentrypoint is executed
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and() // rest api does not have sessions
        .headers().frameOptions().sameOrigin().and() // to enable h2 database;
        .authorizeRequests().antMatchers(
                "/",
              "favicon.ico",
              "/**/*.png",
              "/**/*.gif",
              "/**/*.svg",
              "/**/*.jpg",
              "/**/*.html",
              "/**/*.css",
              "/**/*.js").permitAll() // previous request are allowed without authentication
        .anyRequest().authenticated(); // any request other then previous must be authenticated

    }
}
