package com.school.management.repository;

import com.school.management.model.CourseGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseGroupRepository extends JpaRepository<CourseGroup, Long> {
}
