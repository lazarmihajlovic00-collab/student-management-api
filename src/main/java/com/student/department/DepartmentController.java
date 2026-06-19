package com.student.department;

import com.student.api.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(
            DepartmentService departmentService
    ) {
        this.departmentService = departmentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>>
    createDepartment(
            @RequestBody
            @Valid
            DepartmentRequest request
    ) {

        departmentService.createDepartment(
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Department>> getDepartment(
            @PathVariable Integer id){
        return ResponseEntity
                .ok(ApiResponse.ok(departmentService.getDepartmentById(id)));
    }

}