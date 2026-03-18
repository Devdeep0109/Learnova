package com.example.Learnova.user.config.customUserDetails;


import com.example.Learnova.user.model.UserInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final UserInfo user;

    public CustomUserDetails(UserInfo user) {
        this.user = user;
    }

    public UserInfo getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" +user.getRole()));
    }

    @Override public String getUsername() { return user.getEmail(); }
    @Override public String getPassword() { return user.getPassword(); }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
}

