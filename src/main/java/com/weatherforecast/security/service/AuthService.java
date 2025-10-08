package com.weatherforecast.security.service;

import com.weatherforecast.security.dto.AuthRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailService customUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Generate a JWT token for a user with a username (email)
     * @param request - object with username and password
     * @return String variable with JWT token
     */
    public String generateJwt(AuthRequestDto request) {

        UserDetails authUser = customUserDetailService.loadUserByUsername(request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authUser.getUsername(),
                        authUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtTokenProvider.createToken(request.getUsername());
    }

}
