package com.project.Teampl.config.auth;

import com.project.Teampl.model.user.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// PrincipalDetails 클래스를 만든 목적 :
// 서버 내 특정 영역에 기존 Session이 차지하고 있는데,
// 그 Session 영역 안에 시큐리티 전용 'Security Session'이 존재한다.
// 또 그 Security Session에 들어갈 수 있는 타입(=클래스 변수)는 'Authentication'로,
// Authentication 내 대표적으로 2가지 필드(=타입) 'UserDetails'와 'OAuth2User'이 존재한다.
// 일반 계정은 UserDetails에 회원 정보를 세션 등록하고, 소셜 계정은 OAuth2User에 세션을 등록하기 위함이다.
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails() { }

    // 일반 계정으로 로그인할 때 호출하는 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // 소셜(OAuth) 계정으로 로그인할 때 호출하는 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    // 해당 회원의 권한을 반환하는 메소드!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {
        return user.getUserpw();
    }

    @Override
    public String getUsername() {
        return user.getUserid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
//        return attributes.get("sub");
        return null;
    }
}
