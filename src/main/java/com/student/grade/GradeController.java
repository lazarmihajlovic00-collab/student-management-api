package com.student.grade;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<GradeResponse> create(@Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.addGrade(request));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<GradeResponse>> getByStudent(@PathVariable Integer studentId) {
        return ResponseEntity.ok(gradeService.getGradesByStudent(studentId));
    }
}