package com.project.Teampl.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/css/**", "/js/**",
                "/script/**", "/img/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
            .antMatchers("/tour/**").authenticated() // 로그인 한 사람만 접근 허용
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // 권한(auth)이 "ROLE_ADMIN" 또는 "ROLE_MANAGER"인 계정만 접근 허용
            .anyRequest().permitAll()
            .and()
        .formLogin()
            .loginPage("/login")
            .usernameParameter("userid")
            .passwordParameter("userpw")
            .loginProcessingUrl("/loginProc")
            .defaultSuccessUrl("/tour/all")
            .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
