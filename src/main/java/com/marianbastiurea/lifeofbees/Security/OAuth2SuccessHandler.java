package com.marianbastiurea.lifeofbees.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(jakarta.servlet.http.HttpServletRequest request,
                                        jakarta.servlet.http.HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, jakarta.servlet.ServletException {
        System.out.println("Authentication success handler triggered in OAuth2SuccessHandler.");

        if (authentication == null) {
            System.out.println("Authentication object is null in OAuth2SuccessHandler. Redirecting to login page.");
            response.sendRedirect("/login?error=true");
            return;
        }

        System.out.println("Authentication object in OAuth2SuccessHandler: " + authentication);
        System.out.println("Authorities in OAuth2SuccessHandler: " + authentication.getAuthorities());

        try {
            String targetUrl = determineTargetUrl(authentication);
            System.out.println("Redirecting to target URL in OAuth2SuccessHandler: " + targetUrl);
            response.sendRedirect(targetUrl);
        } catch (Exception e) {
            System.out.println("Error during authentication success handling in OAuth2SuccessHandler: " + e.getMessage());
            throw new jakarta.servlet.ServletException("Failed to handle authentication success in OAuth2SuccessHandler.", e);
        }
    }

    private String determineTargetUrl(Authentication authentication) {
        System.out.println("Determining target URL based on authentication details in OAuth2SuccessHandler.");
        return "/home";
    }
}


