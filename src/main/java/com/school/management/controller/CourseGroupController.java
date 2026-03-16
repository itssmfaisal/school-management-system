package com.school.management.controller;

import com.school.management.model.*;
import com.school.management.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/groups")
public class CourseGroupController {

    private final CourseGroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final UserCourseGroupAssignmentRepository assignmentRepository;
    private final UserCourseAssignmentRepository userCourseAssignmentRepository;

    public CourseGroupController(CourseGroupRepository groupRepository, CourseRepository courseRepository, UserRepository userRepository, UserCourseGroupAssignmentRepository assignmentRepository, UserCourseAssignmentRepository userCourseAssignmentRepository) {
        this.groupRepository = groupRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.userCourseAssignmentRepository = userCourseAssignmentRepository;
    }

    @GetMapping
    public List<CourseGroup> all() { return groupRepository.findAll(); }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseGroup group) {
        CourseGroup saved = groupRepository.save(group);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/{groupId}/add-course/{courseId}")
    public ResponseEntity<?> addCourse(@PathVariable Long groupId, @PathVariable Long courseId, @RequestParam(defaultValue = "false") boolean applyToExisting) {
        Optional<CourseGroup> g = groupRepository.findById(groupId);
        Optional<Course> c = courseRepository.findById(courseId);
        if (g.isEmpty() || c.isEmpty()) return ResponseEntity.notFound().build();
        CourseGroup group = g.get();
        group.getCourses().add(c.get());
        groupRepository.save(group);

        if (applyToExisting) {
            // add course to every user who has this group via assignment snapshot
            List<UserCourseGroupAssignment> assignments = assignmentRepository.findByCourseGroupId(groupId);
            for (UserCourseGroupAssignment a : assignments) {
                a.getAssignedCourses().add(c.get());
                assignmentRepository.save(a);
                // also create explicit user-course assignment if desired
                UserCourseAssignment uca = new UserCourseAssignment();
                uca.setUser(a.getUser());
                uca.setCourse(c.get());
                userCourseAssignmentRepository.save(uca);
            }
        }

        return ResponseEntity.ok(group);
    }

    @PostMapping("/{groupId}/assign-to-user/{userId}")
    public ResponseEntity<?> assignGroupToUser(@PathVariable Long groupId, @PathVariable Long userId) {
        Optional<CourseGroup> g = groupRepository.findById(groupId);
        Optional<User> u = userRepository.findById(userId);
        if (g.isEmpty() || u.isEmpty()) return ResponseEntity.notFound().build();
        UserCourseGroupAssignment a = new UserCourseGroupAssignment();
        a.setUser(u.get());
        a.setCourseGroup(g.get());
        // snapshot current courses
        a.setAssignedCourses(new HashSet<>(g.get().getCourses()));
        assignmentRepository.save(a);
        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/{groupId}/remove-course/{courseId}")
    public ResponseEntity<?> removeCourse(@PathVariable Long groupId, @PathVariable Long courseId) {
        Optional<CourseGroup> g = groupRepository.findById(groupId);
        Optional<Course> c = courseRepository.findById(courseId);
        if (g.isEmpty() || c.isEmpty()) return ResponseEntity.notFound().build();
        CourseGroup group = g.get();
        group.getCourses().remove(c.get());
        groupRepository.save(group);
        return ResponseEntity.ok().build();
    }
}
