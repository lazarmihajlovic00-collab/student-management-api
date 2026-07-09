package com.student.grade;

import com.student.course.Course;
import com.student.course.CourseRepository;
import com.student.exception.CourseNotFoundException;
import com.student.exception.StudentNotFoundException;
import com.student.student.Student;
import com.student.student.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public GradeService(GradeRepository gradeRepository, StudentRepository studentRepository, CourseRepository courseRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public GradeResponse addGrade(GradeRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + request.getStudentId()));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + request.getCourseId()));

        Grade grade = new Grade(request.getValue(), student, course, request.getExamDate());
        Grade savedGrade = gradeRepository.save(grade);

        return new GradeResponse(
                savedGrade.getId(),
                savedGrade.getValue(),
                savedGrade.getExamDate(),
                student.getName(),
                course.getName()
        );
    }

    public List<GradeResponse> getGradesByStudent(Integer studentId) {
        return gradeRepository.findByStudentId(studentId).stream()
                .map(g -> new GradeResponse(g.getId(), g.getValue(), g.getExamDate(),
                        g.getStudent().getName(), g.getCourse().getName()))
                .collect(Collectors.toList());
    }
}