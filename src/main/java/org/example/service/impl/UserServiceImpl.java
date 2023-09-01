package org.example.service.impl;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserRegistrationRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exception.EntityNotFoundException;
import org.example.exception.RegistrationException;
import org.example.mapper.UserMapper;
import org.example.model.Role;
import org.example.model.User;
import org.example.repository.RoleRepository;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(
            UserRegistrationRequestDto requestDto) throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException(
                    "Can not complete registration because user with such email already exist");
        }
        Role userRole = assignRole(requestDto);
        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }

    private Role assignRole(UserRegistrationRequestDto requestDto) {
        Role role;
        if (requestDto.getEmail().startsWith("admin")) {
            role = roleRepository.findByName(Role.RoleName.ADMIN)
                           .orElseThrow(() -> new EntityNotFoundException(
                                   "Can not find role:" + Role.RoleName.ADMIN.name()));
        } else {
            role = roleRepository.findByName(Role.RoleName.USER)
                           .orElseThrow(() -> new EntityNotFoundException(
                                   "Can not find role:" + Role.RoleName.USER.name()));
        }
        return role;
    }
}
