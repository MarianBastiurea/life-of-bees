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

    private Credentials credentials;

    @Autowired
    private OAuth2ClientProperties oAuth2ClientProperties;

    @PostConstruct
    public void init() {
        System.out.println("Init method called.");
        try {
            credentials = loadCredentialsFromJson();
            System.out.println("Google Client ID in init: " + credentials.getGoogle().getClient_id());
            System.out.println("Google Client Secret in init: " + credentials.getGoogle().getClient_secret());

            if (credentials.getGoogle().getClient_id() == null || credentials.getGoogle().getClient_secret() == null) {
                System.out.println("Error: Client ID or Client Secret is null in init.");
                throw new RuntimeException("Client ID or Client Secret is missing in init");
            }
        } catch (Exception e) {
            System.out.println("Error during initialization in init: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        System.out.println("Creating ClientRegistrationRepository bean...");
        try {
            Credentials credentials = loadCredentialsFromJson();
            System.out.println("Google Client ID in ClientRegistrationRepository: " + credentials.getGoogle().getClient_id());
            System.out.println("Google Client Secret in ClientRegistrationRepository: " + credentials.getGoogle().getClient_secret());

            if (credentials.getGoogle().getClient_id() == null || credentials.getGoogle().getClient_secret() == null) {
                System.out.println("Error: Client ID or Client Secret is null in ClientRegistrationRepository.");
                throw new RuntimeException("Client ID or Client Secret is missing in ClientRegistrationRepository ");
            }

            // Creează o înregistrare OAuth2 pentru Google
            System.out.println("Building Google ClientRegistration in ClientRegistrationRepository");
            ClientRegistration googleClientRegistration = ClientRegistration.withRegistrationId("google")
                    .clientId(credentials.getGoogle().getClient_id())
                    .clientSecret(credentials.getGoogle().getClient_secret())
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .scope("profile", "email")
                    .authorizationUri("https://accounts.google.com/o/oauth2/auth")
                    .tokenUri("https://oauth2.googleapis.com/token")
                    .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .clientName("Google")
                    .build();

            System.out.println("Google ClientRegistration created successfully.");
            return new InMemoryClientRegistrationRepository(googleClientRegistration);

        } catch (Exception e) {
            System.out.println("Error creating ClientRegistrationRepository: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private Credentials loadCredentialsFromJson() {
        System.out.println("Loading credentials from JSON in ClientRegistrationRepository...");
        try {
            Credentials credentials = CredentialsLoader.loadCredentials();
            System.out.println("Credentials loaded successfully in OAuth2Config ClientRegistrationRepository.");
            return credentials;
        } catch (Exception e) {
            System.out.println("Error loading credentials in OAuth2Config ClientRegistrationRepository: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Nu s-au putut încărca credențialele in OAuth2Config ClientRegistrationRepository" , e);
        }
    }
    public void printOAuth2Config() {
        // Verificăm dacă client_id și client_secret sunt corect configurate
        System.out.println("Client ID in OAuth2ClientProperties: " + oAuth2ClientProperties.getRegistration().get("google").getClientId());
        System.out.println("Client Secret in OAuth2ClientProperties: " + oAuth2ClientProperties.getRegistration().get("google").getClientSecret());
    }


}
