package com.marianbastiurea.lifeofbees.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import javax.annotation.PostConstruct;
@Configuration
public class OAuth2Config {


    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        System.out.println("Creating ClientRegistrationRepository bean...");
        try {
            String googleClientId = System.getProperty("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID");
            String googleClientSecret = System.getProperty("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET");
            if (googleClientId == null || googleClientSecret == null) {
                System.out.println("Error: Google Client ID or Client Secret is missing.");
                throw new RuntimeException("Google Client ID or Client Secret is missing.");
            }
            ClientRegistration googleClientRegistration = ClientRegistration.withRegistrationId("google")
                    .clientId(googleClientId)
                    .clientSecret(googleClientSecret)
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .scope("profile", "email")
                    .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                    .tokenUri("https://oauth2.googleapis.com/token")
                    .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .clientName("Google")
                    .build();

            String githubClientId = System.getProperty("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_ID");
            String githubClientSecret = System.getProperty("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GITHUB_CLIENT_SECRET");
            if (githubClientId == null || githubClientSecret == null) {
                System.out.println("Error: GitHub Client ID or Client Secret is missing.");
                throw new RuntimeException("GitHub Client ID or Client Secret is missing.");
            }
            ClientRegistration githubClientRegistration = ClientRegistration.withRegistrationId("github")
                    .clientId(githubClientId)
                    .clientSecret(githubClientSecret)
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .scope("read:user", "user:email")
                    .authorizationUri("https://github.com/login/oauth/authorize")
                    .tokenUri("https://github.com/login/oauth/access_token")
                    .userInfoUri("https://api.github.com/user")
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .clientName("GitHub")
                    .build();

            System.out.println("Google and GitHub ClientRegistration created successfully.");
            return new InMemoryClientRegistrationRepository(googleClientRegistration, githubClientRegistration);

        } catch (Exception e) {
            System.out.println("Error creating ClientRegistrationRepository: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void printOAuth2Config() {
        System.out.println("Client ID in OAuth2ClientProperties: " + oAuth2ClientProperties.getRegistration().get("google").getClientId());
        System.out.println("Client Secret in OAuth2ClientProperties: " + oAuth2ClientProperties.getRegistration().get("google").getClientSecret());
    }


}
