package com.student.student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsByEmail(String email);
    List<Student> findByNameContainingIgnoreCase(String name);
    List<Student> findByEmailContainingIgnoreCase(String email);
    Optional<Student> findByEmail(String mail);
}
