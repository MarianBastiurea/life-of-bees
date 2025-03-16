package com.marianbastiurea.lifeofbees.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("Authorization Header: " + request.getHeader("Authorization"));
        logRequestDetails(request);
        String path = request.getRequestURI();
        System.out.println("Request path: " + request.getRequestURI());
        if (isPublicRoute(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("Authorization Header: " + authHeader);
            if (jwtTokenProvider.isTokenValid(token)) {
                authenticateUserFromToken(token);
            } else {
                handleInvalidToken(response);
                return;
            }
        } else {
            System.out.println("Authorization Heather is not valid.");
        }
        filterChain.doFilter(request, response);
    }

    private void logRequestDetails(HttpServletRequest request) {
        System.out.println("=== START doFilterInternal ===");
        System.out.println("Time stamp: " + java.time.LocalDateTime.now());
        System.out.println("URL request: " + request.getRequestURI());
        System.out.println(" HTTP method: " + request.getMethod());

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println(headerName + ": " + request.getHeader(headerName));
        }
    }

    private boolean isPublicRoute(String path) {
        return path.equals("/api/auth/register") || path.equals("/api/auth/signIn")
                || path.equals("/")
                || path.equals("/index.html")
                || path.equals("/favicon.png")
                || path.equals("/manifest.json")
                || path.matches("/static/.*");
    }

    private void authenticateUserFromToken(String token) {
        String userId = jwtTokenProvider.extractUserId(token);
        System.out.println("User ID from token: " + userId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userId, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Authentication for userId: " + userId);
    }

    private void handleInvalidToken(HttpServletResponse response) throws IOException {
        System.out.println("Token invalid or expired.");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Token invalid or expired.");
    }
}