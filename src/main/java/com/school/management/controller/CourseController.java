package com.school.management.controller;

import com.school.management.model.Course;
import com.school.management.repository.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public List<Course> all() { return courseRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        return courseRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Course c) {
        if (c.getCode() != null && courseRepository.findByCode(c.getCode()).isPresent()) {
            return ResponseEntity.badRequest().body("Course code already exists");
        }
        Course saved = courseRepository.save(c);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Course c) {
        return courseRepository.findById(id).map(existing -> {
            existing.setTitle(c.getTitle());
            existing.setDescription(c.getDescription());
            existing.setCode(c.getCode());
            courseRepository.save(existing);
            return ResponseEntity.ok(existing);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!courseRepository.existsById(id)) return ResponseEntity.notFound().build();
        courseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
