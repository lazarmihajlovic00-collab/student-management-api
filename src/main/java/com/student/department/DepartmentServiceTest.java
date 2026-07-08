package com.student.department;

import com.student.exception.DepartmentAlreadyExistsException;
import com.student.exception.DepartmentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void shouldCreateDepartment() {

        DepartmentRequest request = new DepartmentRequest();

        request.setName("Software Engineering");
        request.setCode("SE");

        when(departmentRepository.existsByCode("SE")).thenReturn(false);

        departmentService.createDepartment(request);

        ArgumentCaptor<Department> captor = ArgumentCaptor.forClass(Department.class);

        verify(departmentRepository).save(captor.capture());

        Department savedDepartment = captor.getValue();

        assertEquals("Software Engineering", savedDepartment.getName());

        assertEquals("SE", savedDepartment.getCode()
        );
    }

    @Test
    void shouldThrowWhenDepartmentCodeAlreadyExists() {

        DepartmentRequest request = new DepartmentRequest();

        request.setName("Software Engineering");
        request.setCode("SE");

        when(departmentRepository.existsByCode("SE")).thenReturn(true);

        assertThrows(DepartmentAlreadyExistsException.class,
                () -> departmentService.createDepartment(request)
        );
    }

    @Test
    void shouldReturnDepartmentById() {
        Department department = new Department();
        department.setId(1);
        department.setCode("SE");
        department.setName("Software Engineering");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(department));

        DepartmentResponse result = departmentService.getDepartmentResponseById(1);

        assertNotNull(result);
        assertEquals("Software Engineering", result.getName());
        assertEquals("SE", result.getCode());
    }

    @Test
    void shouldThrowWhenDepartmentNotFound() {
        when(departmentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class,
                () -> departmentService.getDepartmentResponseById(1));
    }
}