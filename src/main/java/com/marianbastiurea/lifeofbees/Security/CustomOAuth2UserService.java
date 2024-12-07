package com.marianbastiurea.lifeofbees.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final OAuth2UserServiceHelper oAuth2UserServiceHelper;

    @Autowired
    public CustomOAuth2UserService(OAuth2UserServiceHelper oAuth2UserServiceHelper) {
        this.oAuth2UserServiceHelper = oAuth2UserServiceHelper;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");
        String providerId = userRequest.getClientRegistration().getRegistrationId();
        oAuth2UserServiceHelper.processOAuthPostLogin(email, providerId);

        return oAuth2User;
    }
}
