package com.marianbastiurea.lifeofbees.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.marianbastiurea.lifeofbees.users.User;
import com.marianbastiurea.lifeofbees.users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2UserServiceHelper oAuth2UserServiceHelper;


    @Autowired
    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider,
                          PasswordEncoder passwordEncode, OAuth2UserServiceHelper oAuth2UserServiceHelper) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncode;
        this.oAuth2UserServiceHelper = oAuth2UserServiceHelper;
    }


    @GetMapping("**")
    public ResponseEntity<?> logUnexpectedEndpoints(HttpServletRequest request) {
        System.out.println("Received request for: " + request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint not found");
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            String userId = userService.registerUser(registerRequest);
            System.out.println("This is UserId from method /register: " + userId);
            String token = jwtTokenProvider.generateToken(userId);
            return ResponseEntity.ok(Map.of("userId", userId, "token", token));
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage())); // Trimite mesaj JSON clar
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during the registration process"));
        }
    }


    @GetMapping("/google-client-id")
    public ResponseEntity<String> getGoogleClientId() {
        String googleClientId = System.getenv("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID");
        return ResponseEntity.ok(googleClientId);
    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.findUserByUsername(loginRequest.getUsername());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
            boolean isPasswordValid = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            if (!isPasswordValid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
            String token = jwtTokenProvider.generateToken(user.getUserId());
            return ResponseEntity.ok(Map.of(
                    "userId", user.getUserId(),
                    "username", user.getUsername(),
                    "token", token
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the login process");
        }
    }

    @PostMapping("/oauth/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> request) {
        System.out.println("Starting Google OAuth authentication process.");
        String idToken = request.get("token");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(System.getenv("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID")))
                    .build();
            String email;
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();
                email = payload.getEmail();
                String username = email.split("@")[0];

                User user = oAuth2UserServiceHelper.processOAuthPostLogin(email, "GOOGLE");
                System.out.println("User processed successfully: " + user);
                String token = jwtTokenProvider.generateToken(user.getUserId());
                System.out.println("Generated JWT token: " + token);
                System.out.println("Returning success response with user details.");
                return ResponseEntity.ok(Map.of(
                        "userId", user.getUserId(),
                        "username", username,
                        "token", token
                ));
            } else {
                System.out.println("OAuth2User principal is null. Returning unauthorized response.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Principal is null");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during Google OAuth authentication: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during Google OAuth authentication");
        }
    }

}
