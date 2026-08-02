package com.student.department;

import com.student.exception.DepartmentAlreadyExistsException;
import com.student.exception.DepartmentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByCode(request.getCode())) {
            throw new DepartmentAlreadyExistsException("Department code already exists");
        }

        Department department = new Department();
        department.setName(request.getName());
        department.setCode(request.getCode());
        departmentRepository.save(department);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentResponseById(Integer id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department not found with id " + id));
        return new DepartmentResponse(department);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getAllDepartments(Pageable pageable) {
        return departmentRepository.findAll(pageable).map(DepartmentResponse::new);
    }
}