package com.school.management.controller;

import com.school.management.dto.CreateEmployeeRequest;
import com.school.management.model.Permission;
import com.school.management.model.Role;
import com.school.management.model.User;
import com.school.management.model.UserType;
import com.school.management.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody CreateEmployeeRequest req) {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User caller = userRepository.findByEmail(principal).orElseThrow();
        boolean isAdmin = caller.getRoles().stream().anyMatch(r -> r == Role.ROLE_ADMIN);
        if (!isAdmin) return ResponseEntity.status(403).body("Only admin can create employees");

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        User u = new User();
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPassword(passwordEncoder.encode(req.getPassword()));
        u.setUserType(UserType.EMPLOYEE);

        if (req.getRoles() != null) {
            Set<Role> roles = req.getRoles().stream().map(String::toUpperCase).map(Role::valueOf).collect(Collectors.toSet());
            u.setRoles(roles);
        }

        if (req.getPermissions() != null) {
            Set<Permission> perms = req.getPermissions().stream().map(String::toUpperCase).map(Permission::valueOf).collect(Collectors.toSet());
            u.setPermissions(perms);
        }

        userRepository.save(u);
        return ResponseEntity.ok(u);
    }
}
