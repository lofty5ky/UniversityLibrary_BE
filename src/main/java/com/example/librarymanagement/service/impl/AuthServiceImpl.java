package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.config.JwtTokenUtil;
import com.example.librarymanagement.dto.request.LoginRequestDTO;
import com.example.librarymanagement.dto.request.UserRequestDTO;
import com.example.librarymanagement.model.Role;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.RoleRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public void registerUser(UserRequestDTO registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền 'USER'"));

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .status(1)
                .phoneNumber(registrationRequest.getPhoneNumber())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);
    }

    @Override
    public String authenticateUser(LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        if (user.getStatus() == 0) {
            throw new RuntimeException("Tài khoản của bạn đã bị khóa");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }

        return jwtTokenUtil.generateToken(user);
    }

    @Override
    public String generateTokenForUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với email: " + email));

        return jwtTokenUtil.generateToken(user);
    }
}
