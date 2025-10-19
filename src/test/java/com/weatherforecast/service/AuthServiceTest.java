package com.weatherforecast.service;

import com.weatherforecast.exception.NotFoundException;
import com.weatherforecast.security.dto.AuthRequestDto;
import com.weatherforecast.security.service.AuthService;
import com.weatherforecast.security.service.CustomUserDetailService;
import com.weatherforecast.security.service.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailService customUserDetailService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthService authService;

    @Test
    void testGenerateJwtAllIsOk() {
        String username = "user@company.com";
        String password = "password";
        String expectedToken = "jwt.token.example";

        AuthRequestDto request = new AuthRequestDto(username, password);
        UserDetails userDetails = mock(UserDetails.class);

        when(customUserDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.createToken(username)).thenReturn(expectedToken);

        SecurityContextHolder.setContext(securityContext);

        String result = authService.generateJwt(request);

        assertNotNull(result);
        assertEquals(expectedToken, result);

        verify(customUserDetailService).loadUserByUsername(username);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(securityContext).setAuthentication(authentication);
        verify(jwtTokenProvider).createToken(username);
    }

    @Test
    void testGenerateJwtWhenUserNotFound() {
        String username = "anyUser@company.com";
        String password = "password123";

        AuthRequestDto request = new AuthRequestDto(username, password);

        when(customUserDetailService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User not found"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authService.generateJwt(request));

        assertEquals("User with email: " + username + " is not registered", exception.getMessage());
    }

    @Test
    void testGenerateJwtWhenInvalidPassword() {
        String username = "user@company.com";
        String password = "wrong456";

        AuthRequestDto request = new AuthRequestDto(username, password);
        UserDetails userDetails = mock(UserDetails.class);

        when(customUserDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.generateJwt(request));

        assertEquals("Bad credentials", exception.getMessage());

        verify(customUserDetailService).loadUserByUsername(username);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testGenerateJwtWhenUsernameIsNull() {
        String username = null;
        String password = "password";

        AuthRequestDto request = new AuthRequestDto(username, password);

        when(customUserDetailService.loadUserByUsername(null))
                .thenThrow(new UsernameNotFoundException("User with email null is not found"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authService.generateJwt(request));

        assertEquals("User with email: null is not registered", exception.getMessage());

        verify(customUserDetailService).loadUserByUsername(null);
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void testGenerateJwtWhenUsernameIsBlank() {
        String username = "  ";
        String password = "password";

        AuthRequestDto request = new AuthRequestDto(username, password);

        when(customUserDetailService.loadUserByUsername(username))
                .thenThrow(new UsernameNotFoundException("User with email  is not found"));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> authService.generateJwt(request));

        assertEquals("User with email:    is not registered", exception.getMessage());

        verify(customUserDetailService).loadUserByUsername(username);
    }

    @Test
    void testGenerateJwtWhenPasswordIsNull() {
        String username = "user@example.com";
        String password = null;

        AuthRequestDto request = new AuthRequestDto(username, password);
        UserDetails userDetails = mock(UserDetails.class);

        when(customUserDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.generateJwt(request));

        assertEquals("Bad credentials", exception.getMessage());

        verify(customUserDetailService).loadUserByUsername(username);
    }

    @Test
    void testGenerateJwtWhenPasswordIsBlank() {
        String username = "user@example.com";
        String password = "  ";

        AuthRequestDto request = new AuthRequestDto(username, password);
        UserDetails userDetails = mock(UserDetails.class);

        when(customUserDetailService.loadUserByUsername(username)).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        BadCredentialsException exception = assertThrows(BadCredentialsException.class,
                () -> authService.generateJwt(request));

        assertEquals("Bad credentials", exception.getMessage());

        verify(customUserDetailService).loadUserByUsername(username);
    }
}