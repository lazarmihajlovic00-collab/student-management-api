package com.student.student;

import com.student.exception.EmailAlreadyExistsException;
import com.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void shouldReturnStudentWhenIdExists() {
        Student student = new Student(1, "Lazar", "lazar@mail.com", 20);

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        Student result = studentService.getStudentById(1);

        assertEquals("Lazar", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class,
                () -> studentService.getStudentById(1));
    }

    @Test
    void shouldCreateStudent() {

        StudentRequest request = new StudentRequest();
        request.setName("Lazar");
        request.setEmail("lazar@mail.com");
        request.setAge(20);

        when(studentRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        studentService.addStudentRequest(request);

        ArgumentCaptor<Student> captor =
                ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(captor.capture());

        Student savedStudent = captor.getValue();

        assertEquals("Lazar", savedStudent.getName());
        assertEquals("lazar@mail.com", savedStudent.getEmail());
        assertEquals(20, savedStudent.getAge());
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {

        when(studentRepository.existsByEmail("test@mail.com"))
                .thenReturn(true);

        StudentRequest studentRequest = new StudentRequest();
        studentRequest.setName("Lazar");
        studentRequest.setAge(20);
        studentRequest.setEmail("test@mail.com");

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> studentService.addStudentRequest(studentRequest)
        );
    }

    @Test
    void shouldDeleteStudent() {

        Integer studentId = 1;

        when(studentRepository.existsById(studentId))
                .thenReturn(true);

        studentService.deleteStudent(studentId);

        verify(studentRepository)
                .deleteById(studentId);
    }

    @Test
    void shouldThrowWhenDeletingNonExistingStudent() {

        Integer studentId = 1;

        when(studentRepository.existsById(studentId))
                .thenReturn(false);

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deleteStudent(studentId)
        );

        verify(studentRepository, never())
                .deleteById(anyInt());
    }

    @Test
    void shouldUpdateStudent() {

        Integer studentId = 1;

        Student existingStudent = new Student(studentId, "Old Name",
                "old@mail.com", 20);

        StudentRequest request = new StudentRequest();
        request.setName("New Name");
        request.setEmail("new@mail.com");
        request.setAge(25);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        studentService.updateStudentRequest(studentId, request);

        assertEquals("New Name", existingStudent.getName());
        assertEquals("new@mail.com", existingStudent.getEmail());
        assertEquals(25, existingStudent.getAge());
    }

    @Test
    void shouldThrowWhenUpdatingWithExistingEmail() {

        Integer studentId = 1;

        Student existingStudent = new Student(studentId, "Lazar",
                        "old@mail.com", 20);

        StudentRequest request = new StudentRequest();
        request.setName("Marko");
        request.setEmail("taken@mail.com");
        request.setAge(25);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));

        when(studentRepository.existsByEmail(
                "taken@mail.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> studentService.updateStudentRequest(studentId, request)
        );
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingStudent() {

        Integer studentId = 1;

        StudentRequest request = new StudentRequest();
        request.setName("Marko");
        request.setEmail("marko@mail.com");
        request.setAge(25);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.updateStudentRequest(studentId, request)
        );
    }

    @Test
    void shouldUpdateStudentWhenEmailRemainsUnchanged() {

        Integer studentId = 1;

        Student existingStudent =
                new Student(
                        studentId,
                        "Lazar",
                        "lazar@mail.com",
                        20
                );

        StudentRequest request =
                new StudentRequest();

        request.setName("Lazar Updated");
        request.setEmail("lazar@mail.com");
        request.setAge(25);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(existingStudent));

        studentService.updateStudentRequest(
                studentId,
                request
        );

        assertEquals(
                "Lazar Updated",
                existingStudent.getName()
        );

        assertEquals(
                "lazar@mail.com",
                existingStudent.getEmail()
        );

        assertEquals(
                25,
                existingStudent.getAge()
        );

        verify(studentRepository, never())
                .existsByEmail(any());
    }
}
