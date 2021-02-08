package com.project.Teampl.config.oauth.provider;

import java.util.Map;

public class FacebookUserInfo implements OAuth2UserInfo {

    // PrincipalOauth2UserService 클래스에서 OAuth2User가 가지고 있는 attributes () :
    // 'oAuth2User.getAttributes()'
    private Map<String, Object> attributes;

    public FacebookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return this.attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    @Override
    public String getEmail() {
        return (this.attributes.get("email") != null) ? this.attributes.get("email").toString() : null;
    }

    @Override
    public String getName() {
        return this.attributes.get("name").toString();
    }
}
