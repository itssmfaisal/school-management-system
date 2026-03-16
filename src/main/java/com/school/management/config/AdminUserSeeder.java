package com.school.management.config;

import com.school.management.model.Permission;
import com.school.management.model.Role;
import com.school.management.model.User;
import com.school.management.model.UserType;
import com.school.management.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(1)
public class AdminUserSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String adminEmail = "admin"; // login with 'admin'
        String adminPassword = "1234";

        if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

        User admin = new User();
        admin.setName("Administrator");
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        admin.setUserType(UserType.EMPLOYEE);

        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_ADMIN);
        admin.setRoles(roles);

        Set<Permission> perms = new HashSet<>(Arrays.asList(Permission.values()));
        admin.setPermissions(perms);

        userRepository.save(admin);
    }
}
