package com.student.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository
        extends JpaRepository<Department, Integer> {

    boolean existsByCode(String code);

    Optional<Department> findByCode(String code);
}