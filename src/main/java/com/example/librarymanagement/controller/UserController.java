package com.example.librarymanagement.controller;

import com.example.librarymanagement.dto.request.UserRequestDTO;
import com.example.librarymanagement.dto.request.UserUpdateStatusDTO;
import com.example.librarymanagement.dto.response.UserResponseDTO;
import com.example.librarymanagement.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    // Lấy tất cả người dùng với phân trang
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<UserResponseDTO> users = userService.getAllUsers(
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId")));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        try {
            Optional<UserResponseDTO> user = userService.findById(userId);
            return user.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        try {
            UserResponseDTO user = userService.createUser(userRequest);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Cập nhật người dùng
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long userId,
                                                      @Valid @RequestBody UserRequestDTO userRequest) {
        try {
            UserResponseDTO updatedUser = userService.updateUser(userId, userRequest);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{userId}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> lockUser(@PathVariable Long userId,
                                      @Valid @RequestBody UserUpdateStatusDTO request) {
        try {
            if (request.getStatus() != 0) {
                return ResponseEntity.badRequest().body("Status phải là 0 để khóa tài khoản.");
            }
            userService.updateUserStatus(userId, 0);
            return ResponseEntity.ok("Tài khoản đã bị khóa.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{userId}/unlock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> unlockUser(@PathVariable Long userId,
                                        @Valid @RequestBody UserUpdateStatusDTO request) {
        try {
            if (request.getStatus() != 1) {
                return ResponseEntity.badRequest().body("Status phải là 1 để mở khóa tài khoản.");
            }

            userService.updateUserStatus(userId, request.getStatus());
            return ResponseEntity.ok("Tài khoản đã được mở khóa.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Xóa người dùng
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> getActiveMembersCount() {
        long count = userService.getActiveMembersCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<UserResponseDTO> users = userService.searchUsers(name, email,
                    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "userId")));
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


