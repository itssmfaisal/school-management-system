package com.school.management.repository;

import com.school.management.model.AssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
    List<AssignmentEntity> findByCourseId(Long courseId);
}
