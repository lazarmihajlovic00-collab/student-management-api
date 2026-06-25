package com.student.course;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository
        extends JpaRepository<Course, Integer> {

    boolean existsByCode(String code);
}
