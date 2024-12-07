package com.marianbastiurea.lifeofbees.Users;

import com.marianbastiurea.lifeofbees.Security.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterRequest registerRequest) {
        Optional<User> existingUser = userRepository.findByUsername(registerRequest.getUsername());
        System.out.println("Existing user: " + existingUser);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public void addGameToUser(User user, String gameId) {
        if (user != null) {
            List<String> gameIds = user.getGameIds();
            if (gameIds == null) {
                gameIds = new ArrayList<>();
                user.setGameIds(gameIds);
            }
            gameIds.add(gameId);
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User is null. Cannot add game.");
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}

