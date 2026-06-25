package com.student.course;

import com.student.exception.CourseAlreadyExistsException;
import com.student.exception.CourseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void shouldCreateCourse() {

        CourseRequest request = new CourseRequest();
        request.setCode("OOP");
        request.setName("Object Oriented Programming");
        request.setCredits(6);

        when(courseRepository.existsByCode("OOP"))
                .thenReturn(false);

        courseService.createCourse(request);

        ArgumentCaptor<Course> captor =
                ArgumentCaptor.forClass(Course.class);

        verify(courseRepository).save(captor.capture());

        Course savedCourse = captor.getValue();

        assertEquals("OOP", savedCourse.getCode());
        assertEquals(
                "Object Oriented Programming",
                savedCourse.getName()
        );
        assertEquals(
                6,
                savedCourse.getCredits()
        );
    }

    @Test
    void shouldThrowWhenCourseCodeAlreadyExists() {

        CourseRequest request = new CourseRequest();
        request.setCode("OOP");
        request.setName("Object Oriented Programming");
        request.setCredits(6);

        when(courseRepository.existsByCode("OOP"))
                .thenReturn(true);

        assertThrows(
                CourseAlreadyExistsException.class,
                () -> courseService.createCourse(request)
        );
    }

    @Test
    void shouldReturnCourseById() {

        Course course = new Course();
        course.setId(1);
        course.setCode("OOP");
        course.setName(
                "Object Oriented Programming"
        );
        course.setCredits(6);

        when(courseRepository.findById(1))
                .thenReturn(Optional.of(course));

        Course result =
                courseService.getCourseById(1);

        assertEquals("OOP", result.getCode());

        assertEquals(
                "Object Oriented Programming",
                result.getName()
        );

        assertEquals(
                6,
                result.getCredits()
        );
    }

    @Test
    void shouldThrowWhenCourseNotFound() {

        when(courseRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseNotFoundException.class,
                () -> courseService.getCourseById(1)
        );
    }
}