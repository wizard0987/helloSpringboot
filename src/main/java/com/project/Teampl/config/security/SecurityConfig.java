package com.project.Teampl.config.security;

import com.project.Teampl.config.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity  // IoC(=스프링 컨테이너)에 Bean 등록 + 시큐리티 필터(Filter)가 등록된다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

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
            .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')") // 권한(auth)이 "ROLE_ADMIN" 또는 "ROLE_MANAGER"인 계정만 접근 허용
            .anyRequest().permitAll()
            .and()
        .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/loginProc")
            .usernameParameter("userid")
            .passwordParameter("userpw")
            .defaultSuccessUrl("/tour/all")
            .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .permitAll()
            .and()
        .oauth2Login()
            .loginPage("/login")    // 이 라인(Line) 이후 구글 로그인이 완료된 뒤의 후 처리가 필요.
            .userInfoEndpoint()     // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당.
            .userService(principalOauth2UserService);   // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체 등록


        // 1. 코드받기(인증),
        // 2. 엑세스토큰(권한),
        // 3. 사용자 프로필 정보 가져오거나,
        // 4-1. 그 정보(이메일, 전화번호, 이름, 아이디)를 토대로 회원가입을 자동으로 바로 진행시킬 수도 있고,
        // 4-2. 쇼핑몰일 경우 집주소 정보 요구 / 백화점인 경우 조건에 따라 회원 등급 발급(?)

        // Tip : oauth client 라이브러리를 사용하면 좋은점!
        // 추가적인 코드 구현 없이, 엑세스 토큰과 사용자 정보를 한번에 가져올 수 있다!
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


}
