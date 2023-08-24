package org.example.service;

import org.example.dto.UserRegistrationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
