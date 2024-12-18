package com.marianbastiurea.lifeofbees.Users;

import com.marianbastiurea.lifeofbees.Security.RegisterRequest;
import com.mongodb.DuplicateKeyException;
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
        String username = registerRequest.getUsername().toLowerCase().trim();
        Optional<User> existingUser = userRepository.findByUsername(username);
        System.out.println("Existing user: " + existingUser);
        if (existingUser.isPresent()) {
            return existingUser.get().getUserId();
        }
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User user = new User();
        user.setUsername(username); // Salvează username-ul normalizat
        user.setPassword(encodedPassword);

        try {
            User savedUser = userRepository.save(user);
            return savedUser.getUserId();
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("Username already exists");
        }
    }


    public void addGameToUser(User user, String gameId) {
        if (user != null) {
            List<String> gamesList = user.getGamesList();
            if (gamesList == null) {
                gamesList = new ArrayList<>();
                user.setGamesList(gamesList);
            }
            if (!gamesList.contains(gameId)) {
                gamesList.add(gameId);
                userRepository.save(user);
                System.out.println("Gameid: "+ gameId+" adăugat la id utilizator: " + user.getUserId());
            } else {
                System.out.println("Gameid: "+gameId+" deja există în lista utilizatorului: " + user.getUserId());
            }
        } else {
            throw new IllegalArgumentException("User is null. Cannot add game.");
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}

