package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.dto.response.RoleResponseDTO;
import com.example.librarymanagement.model.Role;
import com.example.librarymanagement.repository.RoleRepository;
import com.example.librarymanagement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> new RoleResponseDTO(role.getRoleId(), role.getRoleName()))
                .collect(Collectors.toList());
    }
}
