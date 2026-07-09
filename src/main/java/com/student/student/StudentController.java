package com.student.student;

import com.student.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<StudentResponse>>> getAllStudents(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getAllStudents(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getStudentById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentResponseById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addStudentRequest(@Valid @RequestBody StudentRequest request) {
        studentService.addStudentRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(null));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> updateStudentRequest(
            @PathVariable Integer id,
            @Valid @RequestBody StudentRequest request) {
        studentService.updateStudentRequest(id, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/search/name") // <-- ISPRAVLJEN POVRATNI TIP (List<StudentResponse>)
    public ResponseEntity<ApiResponse<List<StudentResponse>>> searchStudentsByName(@RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.searchStudentsByName(name)));
    }

    @GetMapping("/search/email") // <-- ISPRAVLJEN POVRATNI TIP (List<StudentResponse>)
    public ResponseEntity<ApiResponse<List<StudentResponse>>> searchStudentsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.ok(studentService.searchStudentsByEmail(email)));
    }

    @PatchMapping("{id}/graduate")
    public ResponseEntity<ApiResponse<Void>> graduateStudent(@PathVariable Integer id) {
        studentService.graduateStudent(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("{id}/suspend")
    public ResponseEntity<ApiResponse<Void>> suspendStudent(@PathVariable Integer id) {
        studentService.suspendStudent(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateStudent(@PathVariable Integer id) {
        studentService.activateStudent(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("{studentId}/department/{departmentId}")
    public ResponseEntity<ApiResponse<Void>> assignDepartment(
            @PathVariable Integer studentId,
            @PathVariable Integer departmentId) {
        studentService.assignDepartment(studentId, departmentId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PatchMapping("{studentId}/course/{courseId}")
    public ResponseEntity<ApiResponse<Void>> assignCourse(
            @PathVariable Integer studentId,
            @PathVariable Integer courseId) {
        studentService.assignCourse(studentId, courseId);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/{id}/gpa")
    public ResponseEntity<Double> getStudentGPA(@PathVariable Integer id) {
        Double gpa = studentService.calculateGPA(id);
        return ResponseEntity.ok(gpa);
    }

}