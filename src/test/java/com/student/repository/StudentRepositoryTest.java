package com.student.repository;

import com.student.*;
import com.student.student.Student;
import com.student.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void shouldReturnTrueWhenEmailExists() {

        Student student =
                new Student(null,
                        "Lazar",
                        "lazar@mail.com",
                        20);

        studentRepository.save(student);

        boolean exists =
                studentRepository.existsByEmail("lazar@mail.com");

        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {

        boolean exists =
                studentRepository.existsByEmail(
                        "unknown@mail.com");

        assertFalse(exists);
    }
}