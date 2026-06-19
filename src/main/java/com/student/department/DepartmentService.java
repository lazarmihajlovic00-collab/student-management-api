package com.student.department;

import com.student.exception.DepartmentAlreadyExistsException;
import com.student.exception.DepartmentNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(
            DepartmentRepository departmentRepository
    ) {
        this.departmentRepository = departmentRepository;
    }

    public void createDepartment(
            DepartmentRequest request
    ) {

        if (
                departmentRepository.existsByCode(
                        request.getCode()
                )
        ) {
            throw new DepartmentAlreadyExistsException(
                    "Department code already exists"
            );
        }

        Department department =
                new Department();

        department.setName(
                request.getName()
        );

        department.setCode(
                request.getCode()
        );

        departmentRepository.save(
                department
        );
    }

    public Department getDepartmentById(
            Integer id
    ) {

        return departmentRepository.findById(id)
                .orElseThrow(
                        () ->
                                new DepartmentNotFoundException(
                                        "Department with id "
                                                + id
                                                + " not found"
                                )
                );
    }
}