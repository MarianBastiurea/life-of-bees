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
        System.out.println("Starting loadUser method in CustomOAuth2UserService...");

        OAuth2User oAuth2User;
        try {
            oAuth2User = super.loadUser(userRequest);
            System.out.println("Successfully loaded user from provider in CustomOAuth2UserService: " + userRequest.getClientRegistration().getRegistrationId());
        } catch (OAuth2AuthenticationException e) {
            System.out.println("Error while loading user from provider in CustomOAuth2UserService: " + e.getMessage());
            throw e; // Re-throw the exception after logging
        }

        String email = oAuth2User.getAttribute("email");
        if (email == null || email.isEmpty()) {
            System.out.println("Error: Email attribute is null or empty in CustomOAuth2UserService.");
            throw new OAuth2AuthenticationException("Email attribute is missing from OAuth2 user in in CustomOAuth2UserService.");
        } else {
            System.out.println("User email retrieved in CustomOAuth2UserService: " + email);
        }

        String providerId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("Processing OAuth post-login for providerId in CustomOAuth2UserService: " + providerId);

        try {
            oAuth2UserServiceHelper.processOAuthPostLogin(email, providerId);
            System.out.println("Successfully processed OAuth post-login in CustomOAuth2UserService for email: " + email + ", providerId: " + providerId);
        } catch (Exception e) {
            System.out.println("Error during OAuth post-login processing in CustomOAuth2UserService: " + e.getMessage());
            throw new OAuth2AuthenticationException("Failed to process OAuth post-login in CustomOAuth2UserService.");
        }

        System.out.println("Finished loadUser method in CustomOAuth2UserService.");
        return oAuth2User;
    }
}
