package com.student;

import com.student.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Student>>> getAllStudents(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable){
        return ResponseEntity.ok(ApiResponse.ok(studentService.getAllStudents(pageable)));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Student>> getStudentById(@PathVariable Integer id){
       return ResponseEntity.ok(ApiResponse.ok(studentService.getStudentById(id)));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable Integer id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addStudentRequest(@RequestBody @Valid StudentRequest request){
        studentService.addStudentRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(null));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> updateStudentRequest(@PathVariable Integer id,
                                     @RequestBody @Valid StudentRequest request){
        studentService.updateStudentRequest(id, request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}
