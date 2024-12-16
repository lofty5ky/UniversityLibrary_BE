package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.request.UserRequestDTO;
import com.example.librarymanagement.dto.response.UserResponseDTO;
import com.example.librarymanagement.model.Role;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.RoleRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.UserService;
import com.example.librarymanagement.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(this::mapToResponseDTO);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        String roleName = userDTO.getRole() != null ? userDTO.getRole() : "user";

        Role userRole = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role '" + roleName + "' not found"));

        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .status(1)
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);
        return mapToResponseDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserRequestDTO userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());

        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    public void updateUserStatus(Long userId, Integer status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "admin".equalsIgnoreCase(role.getRoleName()));
        if (isAdmin) {
            throw new RuntimeException("Không thể cập nhật trạng thái cho người dùng có vai trò 'admin'.");
        }

        user.setStatus(status);
        userRepository.save(user);
    }

    @Override
    public Optional<UserResponseDTO> findById(Long userId) {
        return userRepository.findById(userId).map(this::mapToResponseDTO);
    }


    @Override
    public void deleteUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "admin".equalsIgnoreCase(role.getRoleName()));

        if (isAdmin) {
            throw new RuntimeException("Không thể xóa người dùng với role 'admin'.");
        }
        userRepository.delete(user);
    }

    @Override
    public Page<UserResponseDTO> searchUsers(String name, String email, Pageable pageable) {
        // Áp dụng Specification với các điều kiện tìm kiếm
        Specification<User> specification = Specification.where(UserSpecification.hasName(name))
                .and(UserSpecification.hasEmail(email));

        Page<User> userPage = userRepository.findAll(specification, pageable);
        return userPage.map(this::mapToResponseDTO);
    }

    public long getActiveMembersCount(){
        return userRepository.countActiveMembers();
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .roleName(user.getRoles().stream().findFirst().get().getRoleName())
                .build();
    }
}

