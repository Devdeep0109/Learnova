package com.example.Learnova.user.config.customUserDetails;

import com.example.Learnova.user.model.UserInfo;
import com.example.Learnova.user.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepo repo;

    public UserDetails loadUserByUsername(String email){
        UserInfo user = repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }
}
