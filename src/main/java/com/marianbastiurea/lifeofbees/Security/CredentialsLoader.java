package com.marianbastiurea.lifeofbees.Security;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class CredentialsLoader {
    public static Credentials loadCredentials() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File("/Users/marianbastiurea/Documents/credentials.json"), Credentials.class);
    }

    public static void main(String[] args) {
        try {
            Credentials credentials = loadCredentials();
            System.out.println("Loaded Credentials from JSON: " + credentials);
            String googleClientId = credentials.getGoogle().getClient_id();
            String googleClientSecret = credentials.getGoogle().getClient_secret();
            System.out.println("Google Client ID din Credentials: " + googleClientId);
            System.out.println("Google Client Secret din Credentials: " + googleClientSecret);
                    } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
