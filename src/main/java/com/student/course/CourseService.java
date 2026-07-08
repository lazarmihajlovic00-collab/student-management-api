package com.student.course;

import com.student.exception.CourseAlreadyExistsException;
import com.student.exception.CourseNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional 
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void createCourse(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new CourseAlreadyExistsException("Course code already exists");
        }

        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setCredits(request.getCredits());
        course.setMaxStudents(request.getMaxStudents());

        courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseResponseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with id " + id + " not found"));
        return new CourseResponse(course);
    }
}