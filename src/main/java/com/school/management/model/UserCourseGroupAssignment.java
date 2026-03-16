package com.school.management.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
public class UserCourseGroupAssignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private CourseGroup courseGroup;

    // snapshot of courses assigned at time of assignment
    @ManyToMany
    @JoinTable(name = "user_group_assigned_courses",
            joinColumns = @JoinColumn(name = "assignment_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private Set<Course> assignedCourses = new HashSet<>();

    private boolean active = true;

    private Instant assignedAt = Instant.now();

    public UserCourseGroupAssignment() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public CourseGroup getCourseGroup() { return courseGroup; }
    public void setCourseGroup(CourseGroup courseGroup) { this.courseGroup = courseGroup; }
    public Set<Course> getAssignedCourses() { return assignedCourses; }
    public void setAssignedCourses(Set<Course> assignedCourses) { this.assignedCourses = assignedCourses; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getAssignedAt() { return assignedAt; }
    public void setAssignedAt(Instant assignedAt) { this.assignedAt = assignedAt; }
}
