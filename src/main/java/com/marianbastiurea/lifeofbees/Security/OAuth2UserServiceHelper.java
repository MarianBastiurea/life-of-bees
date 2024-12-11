package com.marianbastiurea.lifeofbees.Security;

import com.marianbastiurea.lifeofbees.Users.User;
import com.marianbastiurea.lifeofbees.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OAuth2UserServiceHelper {
    private final UserRepository userRepository;

    @Autowired
    public OAuth2UserServiceHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println("OAuth2UserServiceHelper initialized with UserRepository.");
    }

    public User processOAuthPostLogin(String email, String providerId) {
        System.out.println("Starting processOAuthPostLogin in OAuth2UserServiceHelper for email: " + email + ", providerId: " + providerId);

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            System.out.println("User found in database in OAuth2UserServiceHelper: " + userOptional.get());
            return userOptional.get();
        }

        System.out.println("No existing user found for email in OAuth2UserServiceHelper: " + email + ". Creating a new user.");
        try {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setProvider("GOOGLE");
            newUser.setProvider(providerId);
            User savedUser = userRepository.save(newUser);
            System.out.println("New user successfully created in OAuth2UserServiceHelper: " + savedUser);
            return savedUser;
        } catch (Exception e) {
            System.out.println("Error while creating or saving new user in OAuth2UserServiceHelper: " + e.getMessage());
            throw e;
        }
    }
}

