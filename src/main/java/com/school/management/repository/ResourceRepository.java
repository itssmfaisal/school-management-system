package com.school.management.repository;

import com.school.management.model.ResourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<ResourceEntity, Long> {
    List<ResourceEntity> findByCourseId(Long courseId);
}
