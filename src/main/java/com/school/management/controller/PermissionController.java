package com.school.management.controller;

import com.school.management.dto.PermissionUpdateRequest;
import com.school.management.model.Permission;
import com.school.management.service.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/{id}/permissions")
    public ResponseEntity<Set<Permission>> updatePermissions(@PathVariable("id") Long id,
                                                             @RequestBody PermissionUpdateRequest req) {
        Set<Permission> updated = permissionService.updateUserPermissions(id, req);
        return ResponseEntity.ok(updated);
    }
}
