package com.school.management.security;

import com.school.management.model.User;
import com.school.management.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true,
                mapRolesAndPermissionsToAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesAndPermissionsToAuthorities(User user) {
        // Roles are prefixed with ROLE_ as usual; permissions are added as plain authorities
        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
            .map(r -> new SimpleGrantedAuthority(r.name()))
            .collect(Collectors.toSet());

        authorities.addAll(user.getPermissions().stream()
            .map(p -> new SimpleGrantedAuthority(p.name()))
            .collect(Collectors.toSet()));

        return authorities;
    }

    // Provide a method to get permission authorities (used by security checks elsewhere if needed)
    public Collection<? extends GrantedAuthority> getPermissionAuthorities(User user) {
        return user.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.name())).collect(Collectors.toSet());
    }
}
