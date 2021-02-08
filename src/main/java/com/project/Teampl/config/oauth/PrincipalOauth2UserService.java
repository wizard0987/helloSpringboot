package com.project.Teampl.config.oauth;

import com.project.Teampl.config.auth.PrincipalDetails;
import com.project.Teampl.config.oauth.provider.*;
import com.project.Teampl.model.user.User;
import com.project.Teampl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

//    private final PasswordEncoder encoder;
    private final UserRepository userRepository;

    // 구글로부터 박은 userRequest 데이터에 대한 후처리되는 메소드
    // 이 메소드 호출 종료 시, '@AuthenticationPrincipal' 어노테이션이 생성된다!!
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("getClientRegistration : " + userRequest.getClientRegistration());
        System.out.println("getAccessToken : " + userRequest.getAccessToken().getTokenValue());
//        System.out.println("userRequest : " + super.loadUser(userRequest).getAttributes());
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("userRequest : " + oAuth2User.getAttributes());

        // OAuth 계정을 강제로 회원가입
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            System.out.println("구글 로그인");
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페북 로그인");
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            System.out.println("카카오 로그인");
            oAuth2UserInfo = new KakaoUserInfo((Map) oAuth2User.getAttributes());
        }

        String provider     = oAuth2UserInfo.getProvider(); // google, facebook, naver 등
        String providerId   = oAuth2UserInfo.getProviderId(); // sub : 128543...(google의 PK)
        String username     = oAuth2UserInfo.getName();
        String userid       = provider + "_" + providerId;  // google_128543...(중복 방지를 위해...)
//        String userpw       = "아이고의미없다";
        String email        = oAuth2UserInfo.getEmail();
        String role         = "ROLE_USER";

//        String provider     = userRequest.getClientRegistration().getRegistrationId(); // google, facebook, naver 등
//        String providerId   = oAuth2User.getAttribute("sub"); // sub : 128543...(google의 PK)
//        String username     = oAuth2User.getAttribute("name");
//        String userid       = provider + "_" + providerId;  // google_128543...(중복 방지를 위해...)
////        String userpw       = "아이고의미없다";
//        String email        = oAuth2User.getAttribute("email");
//        String role         = "ROLE_USER";

        User findUser = userRepository.findByUserid(userid);
        // 이미 회원가입이 되어있을 수도 있으므로 확인 절차
        if(findUser == null) {
            // 회원가입이 안한 상태인 경우에는 회원가입 창으로 이동시켜야할 듯...
            findUser = User.builder()
                    .username(username)
                    .userid(userid)
//                    .userpw(userpw)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .regdate(LocalDateTime.now())
                    .build();
            userRepository.save(findUser);
        } else {
            // 회원가입이 이미 되어 있는 경우
        }

        return new PrincipalDetails(findUser, oAuth2User.getAttributes());
//        return super.loadUser(userRequest);
    }
}
