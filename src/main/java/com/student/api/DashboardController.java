package com.student.api; // ili tvoj paket

import com.student.api.ApiResponse;
import com.student.course.CourseRepository;
import com.student.department.DepartmentRepository;
import com.student.grade.GradeRepository;
import com.student.student.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final GradeRepository gradeRepository;

    public DashboardController(StudentRepository studentRepository, CourseRepository courseRepository, DepartmentRepository departmentRepository, GradeRepository gradeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
        this.gradeRepository = gradeRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<Map<String, Long>>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("students", studentRepository.count());
        stats.put("courses", courseRepository.count());
        stats.put("departments", departmentRepository.count());
        stats.put("grades", gradeRepository.count());
        return ResponseEntity.ok(ApiResponse.ok(stats));
    }
}