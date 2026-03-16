package com.school.management.controller;

import com.school.management.model.PermissionGroup;
import com.school.management.model.Permission;
import com.school.management.model.User;
import com.school.management.repository.PermissionGroupRepository;
import com.school.management.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/permission-groups")
public class PermissionGroupController {

    private final PermissionGroupRepository groupRepository;
    private final UserRepository userRepository;

    public PermissionGroupController(PermissionGroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<PermissionGroup> create(@RequestBody PermissionGroup group) {
        // simple admin check
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User caller = userRepository.findByEmail(principal).orElseThrow();
        boolean isAdmin = caller.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).build();

        PermissionGroup saved = groupRepository.save(group);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{groupId}/assign/{userId}")
    public ResponseEntity<?> assignGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User caller = userRepository.findByEmail(principal).orElseThrow();
        boolean isAdmin = caller.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("Only admin can assign permission groups");

        PermissionGroup group = groupRepository.findById(groupId).orElseThrow();
        User target = userRepository.findById(userId).orElseThrow();

        Set<Permission> perms = target.getPermissions();
        perms.addAll(group.getPermissions());
        target.setPermissions(perms);
        userRepository.save(target);

        return ResponseEntity.ok(target.getPermissions());
    }
}
