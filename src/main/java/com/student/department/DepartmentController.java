package com.student.department;

import com.student.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createDepartment(@Valid @RequestBody DepartmentRequest request) {
        departmentService.createDepartment(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(null));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartmentById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.ok(departmentService.getDepartmentResponseById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepartmentResponse>>> getAllDepartments(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC)  Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(departmentService.getAllDepartments(pageable)));
    }

}