package com.marianbastiurea.lifeofbees.Security;

import com.marianbastiurea.lifeofbees.Users.User;
import com.marianbastiurea.lifeofbees.Users.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")

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
        this.oAuth2UserServiceHelper=oAuth2UserServiceHelper;
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
            System.out.println("Acesta e userId: " + userId);
            String token = jwtTokenProvider.generateToken(userId);
            return ResponseEntity.ok(Map.of("userId", userId, "token", token));
        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the registration process");
        }
    }

    @GetMapping("/google-client-id")
    public ResponseEntity<String> getGoogleClientId() {
        String googleClientId = System.getProperty("SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID");
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
            String token = jwtTokenProvider.generateToken(user.getId());

            return ResponseEntity.ok(Map.of(
                    "userId", user.getId(),
                    "username", user.getUsername(),
                    "token", token
            ));
        } catch (Exception e) {
            e.printStackTrace(); // Sau logare detaliată a erorii
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during the login process");
        }
    }
    @PostMapping("/oauth/google")
    public ResponseEntity<?> authenticateWithGoogle(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println("Starting Google OAuth authentication process.");

        try {
            if (principal == null) {
                System.out.println("OAuth2User principal is null. Returning unauthorized response.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Principal is null");
            }
            Map<String, Object> attributes = principal.getAttributes();
            System.out.println("OAuth2User attributes: " + attributes);
            String email = (String) attributes.get("email");
            String providerId = (String) attributes.get("sub");
            System.out.println("Extracted email: " + email + ", providerId: " + providerId);

            if (email == null || providerId == null) {
                System.out.println("Email or providerId is missing. Returning bad request response.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Email or providerId is missing");
            }
            User user = oAuth2UserServiceHelper.processOAuthPostLogin(email, providerId);
            System.out.println("User processed successfully: " + user);
            String token = jwtTokenProvider.generateToken(user.getId());
            System.out.println("Generated JWT token: " + token);
            System.out.println("Returning success response with user details.");
            return ResponseEntity.ok(Map.of(
                    "userId", user.getId(),
                    "email", user.getEmail(),
                    "token", token
            ));
        } catch (Exception e) {
            System.out.println("An error occurred during Google OAuth authentication: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during Google OAuth authentication");
        }
    }

}
