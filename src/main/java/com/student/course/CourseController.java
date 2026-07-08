package com.student.course;

import com.student.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCourse(@Valid @RequestBody CourseRequest request) {
        courseService.createCourse(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponse>> getCourse(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(courseService.getCourseResponseById(id)));
    }
}