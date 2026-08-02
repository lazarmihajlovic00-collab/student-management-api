package com.student.course;

import com.student.department.Department;
import com.student.department.DepartmentRepository;
import com.student.exception.CourseAlreadyExistsException;
import com.student.exception.CourseNotFoundException;
import com.student.exception.DepartmentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional 
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    public CourseService(CourseRepository courseRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }
    public void createCourse(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new CourseAlreadyExistsException("Course code already exists");
        }
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found"));
        Course course = new Course();
        course.setName(request.getName());
        course.setCode(request.getCode());
        course.setCredits(request.getCredits());
        course.setMaxStudents(request.getMaxStudents());
        course.setDepartment(department);
        courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseResponseById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with id " + id + " not found"));
        return new CourseResponse(course);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(CourseResponse::new);
    }
}