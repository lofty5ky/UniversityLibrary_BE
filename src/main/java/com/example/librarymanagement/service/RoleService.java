package com.example.librarymanagement.service;

import com.example.librarymanagement.dto.response.RoleResponseDTO;
import com.example.librarymanagement.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByRoleName(String roleName);

    List<RoleResponseDTO> getAllRoles();
}
