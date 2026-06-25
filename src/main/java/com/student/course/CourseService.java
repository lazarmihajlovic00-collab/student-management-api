package com.student.course;

import com.student.exception.CourseAlreadyExistsException;
import com.student.exception.CourseNotFoundException;
import com.student.student.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void createCourse(CourseRequest request) {

        if(courseRepository.existsByCode(request.getCode())) {
            throw new CourseAlreadyExistsException(
                    "Course code already exists"
            );
        }

        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setCredits(request.getCredits());

        courseRepository.save(course);
    }

    public Course getCourseById(Integer id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new CourseNotFoundException("Course with id " + id + " not found")
        );
    }
}
