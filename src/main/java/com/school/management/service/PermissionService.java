package com.school.management.service;

import com.school.management.dto.PermissionUpdateRequest;
import com.school.management.model.Permission;
import com.school.management.model.User;
import com.school.management.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    private final UserRepository userRepository;

    public PermissionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public Set<Permission> updateUserPermissions(Long targetUserId, PermissionUpdateRequest req) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.findByEmail(principal).orElseThrow();

        User target = userRepository.findById(targetUserId).orElseThrow();

        boolean isAdmin = current.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));

        Set<Permission> currentPerms = new HashSet<>(current.getPermissions());

        // If not admin, require ASSIGN_PERMISSIONS permission
        if (!isAdmin && !currentPerms.contains(Permission.ASSIGN_PERMISSIONS)) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to assign permissions");
        }

        // Build sets to add/remove
        Set<Permission> toAdd = Optional.ofNullable(req.getAdd()).orElse(List.of()).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(Permission::valueOf)
                .collect(Collectors.toSet());

        Set<Permission> toRemove = Optional.ofNullable(req.getRemove()).orElse(List.of()).stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(String::toUpperCase)
                .map(Permission::valueOf)
                .collect(Collectors.toSet());

        // If not admin, ensure current user can only assign permissions they possess
        if (!isAdmin) {
            if (!currentPerms.containsAll(toAdd) || !currentPerms.containsAll(toRemove)) {
                throw new org.springframework.security.access.AccessDeniedException("Cannot assign permissions that you do not have");
            }
        }

        Set<Permission> targetPerms = new HashSet<>(target.getPermissions());
        targetPerms.addAll(toAdd);
        targetPerms.removeAll(toRemove);

        target.setPermissions(targetPerms);
        userRepository.save(target);

        return targetPerms;
    }
}
