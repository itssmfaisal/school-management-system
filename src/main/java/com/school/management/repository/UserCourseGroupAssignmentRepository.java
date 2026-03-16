package com.school.management.repository;

import com.school.management.model.UserCourseGroupAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseGroupAssignmentRepository extends JpaRepository<UserCourseGroupAssignment, Long> {
    List<UserCourseGroupAssignment> findByCourseGroupId(Long groupId);
}
