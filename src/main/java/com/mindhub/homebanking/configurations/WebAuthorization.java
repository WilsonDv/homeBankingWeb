package com.mindhub.homebanking.configurations;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/web/scripts/**").permitAll()
                .antMatchers("/web/styles.css").permitAll()
                .antMatchers("/assets/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()

                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console/**").hasAuthority("ADMIN")
                .antMatchers("/manager.html").hasAuthority("ADMIN")
                .antMatchers("/manager.js").hasAuthority("ADMIN")
                .antMatchers("/api/clients").hasAuthority("ADMIN")
                .antMatchers("/accounts").hasAuthority("ADMIN")

                .antMatchers(HttpMethod.GET,"/api/clients/current/**").hasAnyAuthority("CLIENT, ADMIN")
                .antMatchers(HttpMethod.GET,"/api/loans").hasAuthority("CLIENT")
                .antMatchers("/web/**").hasAnyAuthority("CLIENT, ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

    }

        private void clearAuthenticationAttributes(HttpServletRequest request) {

            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            }
        }
}

