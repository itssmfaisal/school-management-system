package com.school.management.repository;

import com.school.management.model.UserCourseAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseAssignmentRepository extends JpaRepository<UserCourseAssignment, Long> {
    List<UserCourseAssignment> findByUserId(Long userId);
}
