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
    }

    public User processOAuthPostLogin(String email, String providerId) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setProvider("GOOGLE");
        newUser.setProvider(providerId);
        return userRepository.save(newUser);
    }
}
