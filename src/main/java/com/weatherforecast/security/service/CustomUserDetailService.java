package com.weatherforecast.security.service;

import com.weatherforecast.repository.UserRepository;
import com.weatherforecast.security.entity.MyUserToUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .map(user -> new MyUserToUserDetails(user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + userEmail + " not found"));
    }
}

