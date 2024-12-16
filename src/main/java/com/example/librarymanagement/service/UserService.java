package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.request.UserRequestDTO;
import com.example.librarymanagement.dto.response.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    Optional<UserResponseDTO> findById(Long userId);

    UserResponseDTO createUser(UserRequestDTO userRequest);

    UserResponseDTO updateUser(Long userId, UserRequestDTO userRequest);

    void  updateUserStatus(Long userId, Integer status);

    void deleteUserById(Long userId);

    Page<UserResponseDTO> searchUsers(String name, String email, Pageable pageable);

    long getActiveMembersCount();
}
