package com.marianbastiurea.lifeofbees.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfig(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("Configuring SecurityFilterChain in SecurityConfig...");

        http
                .cors(cors -> {
                    System.out.println("Enabling CORS configuration in SecurityConfig...");
                    cors.configurationSource(corsConfigurationSource());
                })
                .csrf(csrf -> {
                    System.out.println("Disabling CSRF protection in SecurityConfig...");
                    csrf.disable();
                })
                .authorizeHttpRequests(auth -> {
                    System.out.println("Setting up authorization rules in SecurityConfig...");
                    auth.requestMatchers("/api/auth/register", "/api/auth/signin", "/oauth2/**").permitAll()
                            .anyRequest().authenticated();
                })
                .oauth2Login(oauth2 -> {
                    System.out.println("Configuring OAuth2 login in SecurityConfig...");
                    oauth2.loginPage("/login")
                            .userInfoEndpoint(userInfo -> {
                                System.out.println("Configuring UserInfoEndpoint in SecurityConfig...");
                                userInfo.userService(customOAuth2UserService);
                            })
                            .successHandler(oAuth2SuccessHandler)
                            .clientRegistrationRepository(clientRegistrationRepository);
                })
                .sessionManagement(session -> {
                    System.out.println("Setting session creation policy to STATELESS in SecurityConfig...");
                    session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS);
                })
                .headers(headers -> {
                    System.out.println("Disabling frame options headers in SecurityConfig...");
                    headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        System.out.println("SecurityFilterChain configuration completed in SecurityConfig.");
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("Initializing BCryptPasswordEncoder in SecurityConfig...");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        System.out.println("Creating AuthenticationManager in SecurityConfig...");
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        System.out.println("AuthenticationManager created in SecurityConfig: " + authenticationManager);
        return authenticationManager;
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        System.out.println("Creating CorsConfigurationSource in SecurityConfig...");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000"); // Domeniul frontend-ului
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
