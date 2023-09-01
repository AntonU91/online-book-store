package org.example.service.impl;

import org.example.security.JwtUtil;
import org.example.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserLoginRequestDto;
import org.example.dto.UserLoginResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserLoginResponseDto authenticate(UserLoginRequestDto userLoginRequestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginRequestDto.getEmail(),
                        userLoginRequestDto.getPassword()));
        // TODO Find out whether delete it or not
        String token = jwtUtil.generateToken(userLoginRequestDto.getEmail());
        return new UserLoginResponseDto(token);
    }
}
