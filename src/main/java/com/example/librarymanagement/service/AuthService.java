package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.LoginRequestDTO;
import com.example.librarymanagement.dto.request.UserRequestDTO;

public interface AuthService {
    void registerUser(UserRequestDTO registrationRequest);

    String authenticateUser(LoginRequestDTO loginRequest);

    String generateTokenForUser(String email);
}
