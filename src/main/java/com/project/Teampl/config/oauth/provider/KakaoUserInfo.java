package com.project.Teampl.config.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return this.attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        Map<String, Object> sub = (Map) this.attributes.get("kakao_account");
        return sub.get("email").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> sub = (Map) attributes.get("kakao_account");
        Map<String, Object> sub2 = (Map) sub.get("profile");
        return sub2.get("nickname").toString();
    }
}
