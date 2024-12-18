package com.marianbastiurea.lifeofbees.Users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Document(collection = "users")
public class User implements UserDetails {

    @Id
    private String userId;

    @Indexed(unique = true)
    private String username;

    private String password;
    private List<String> gamesList = new ArrayList<>();

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Provider cannot be blank")
    private String provider;

    private String providerId;
    public User() {
    }

    public User(String username, String password, List<String> gamesList) {
        this.username = username;
        this.password = password;
        this.gamesList = gamesList != null ? new ArrayList<>(gamesList) : new ArrayList<>();
    }

    public User(List<String> gamesList, String email, String provider, String providerId) {
        this.gamesList = gamesList;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getGamesList() {
        return gamesList;
    }

    public void setGamesList(List<String> gamesList) {
        this.gamesList = gamesList;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

