package com.school.management.controller;

import com.school.management.dto.ProfileResponse;
import com.school.management.model.User;
import com.school.management.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProfileController {

    private final UserRepository userRepository;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public ProfileResponse profile() {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(principal).orElseThrow();

        ProfileResponse resp = new ProfileResponse();
        resp.setId(user.getId());
        resp.setName(user.getName());
        resp.setEmail(user.getEmail());
        resp.setEnabled(user.isEnabled());
        resp.setCreatedAt(user.getCreatedAt());
        resp.setRoles(user.getRoles());
        resp.setPermissions(user.getPermissions());

        return resp;
    }
}
