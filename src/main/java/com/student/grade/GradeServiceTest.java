package com.student.grade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.student.course.Course;
import com.student.course.CourseRepository;
import com.student.student.Student;
import com.student.student.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock private GradeRepository gradeRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks private GradeService gradeService;

    @Test
    void shouldSuccessfullyAddGrade() {
        GradeRequest request = new GradeRequest(1, 1, 9, LocalDate.now());
        Student student = new Student();
        student.setName("Lazar");
        Course course = new Course();
        course.setName("Programiranje");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(i -> i.getArguments()[0]);

        GradeResponse response = gradeService.addGrade(request);

        assertNotNull(response);
        assertEquals(9, response.getValue());
        assertEquals("Lazar", response.getStudentName());
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }
}