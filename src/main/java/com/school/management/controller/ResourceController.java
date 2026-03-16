package com.school.management.controller;

import com.school.management.model.ResourceEntity;
import com.school.management.repository.ResourceRepository;
import com.school.management.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceRepository resourceRepository;
    private final MailService mailService;

    public ResourceController(ResourceRepository resourceRepository, MailService mailService) {
        this.resourceRepository = resourceRepository;
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ResourceEntity r) {
        ResourceEntity saved = resourceRepository.save(r);
        // TODO: Notify students enrolled in the course
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ResourceEntity>> listForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(resourceRepository.findByCourseId(courseId));
    }
}
