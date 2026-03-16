package com.school.management.controller;

import com.school.management.model.*;
import com.school.management.repository.*;
import com.school.management.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final MailService mailService;

    public AssignmentController(AssignmentRepository assignmentRepository, SubmissionRepository submissionRepository, CourseRepository courseRepository, UserRepository userRepository, MailService mailService) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<?> createAssignment(@PathVariable Long courseId, @RequestBody AssignmentEntity a, Authentication auth) {
        Optional<Course> c = courseRepository.findById(courseId);
        if (c.isEmpty()) return ResponseEntity.notFound().build();
        a.setCourse(c.get());
        // createdBy should be set based on auth principal
        assignmentRepository.save(a);
        // TODO: notify students enrolled in course
        return ResponseEntity.ok(a);
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<?> submit(@PathVariable Long assignmentId, @RequestBody Submission s, Authentication auth) {
        Optional<AssignmentEntity> a = assignmentRepository.findById(assignmentId);
        if (a.isEmpty()) return ResponseEntity.notFound().build();
        s.setAssignment(a.get());
        // set student from auth if possible
        submissionRepository.save(s);
        return ResponseEntity.ok(s);
    }

    @PostMapping("/{submissionId}/grade")
    public ResponseEntity<?> grade(@PathVariable Long submissionId, @RequestParam Double marks) {
        Optional<Submission> s = submissionRepository.findById(submissionId);
        if (s.isEmpty()) return ResponseEntity.notFound().build();
        Submission sub = s.get();
        sub.setMarks(marks);
        submissionRepository.save(sub);
        // notify student by email if available
        if (sub.getStudent() != null && sub.getStudent().getEmail() != null) {
            mailService.sendToMany(List.of(sub.getStudent().getEmail()), "Marks updated", "Your marks have been updated for assignment.");
        }
        return ResponseEntity.ok(sub);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<AssignmentEntity>> listForCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(assignmentRepository.findByCourseId(courseId));
    }
}
