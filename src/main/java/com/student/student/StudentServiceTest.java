package com.student.student;

import com.student.course.Course;
import com.student.course.CourseRepository;
import com.student.department.Department;
import com.student.department.DepartmentRepository;
import com.student.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private StudentService studentService;

    @Mock
    private CourseRepository courseRepository;

    @Test
    void shouldReturnStudentWhenIdExists() {

        Student student = new Student();
        student.setId(1);
        student.setName("Lazar");
        student.setEmail("lazar@example.com");

        when(studentRepository.findById(1)).thenReturn(Optional.of(student));


        StudentResponse result = studentService.getStudentResponseById(1);


        assertNotNull(result);
        assertEquals("Lazar", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenStudentNotFound() {

        when(studentRepository.findById(1)).thenReturn(Optional.empty());


        assertThrows(StudentNotFoundException.class, () -> studentService.getStudentResponseById(1));
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

    @Test
    void shouldGraduateStudent() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.ACTIVE);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        Course course1 = new Course();
        course1.setCredits(90);

        Course course2 = new Course();
        course2.setCredits(90);

        student.getCourses().add(course1);
        student.getCourses().add(course2);

        studentService.graduateStudent(studentId);

        assertEquals(
                StudentStatus.GRADUATED,
                student.getStatus()
        );

        assertEquals(
                java.time.LocalDate.now(),
                student.getGraduationDate()
        );
    }

    @Test
    void shouldThrowWhenGraduatingNonExistingStudent() {

        Integer studentId = 1;

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.graduateStudent(studentId)
        );
    }

    @Test
    void shouldThrowWhenStudentAlreadyGraduated() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.GRADUATED);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        assertThrows(
                StudentAlreadyGraduatedException.class,
                () -> studentService.graduateStudent(studentId)
        );
    }

    @Test
    void shouldSuspendStudent() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.ACTIVE);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        studentService.suspendStudent(studentId);

        assertEquals(
                StudentStatus.SUSPENDED,
                student.getStatus()
        );
    }

    @Test
    void shouldThrowWhenStudentAlreadySuspended() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.SUSPENDED);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        assertThrows(
                StudentStatusChangeNotAllowedException.class,
                () -> studentService.suspendStudent(studentId)
        );
    }

    @Test
    void shouldThrowWhenSuspendingGraduatedStudent() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.GRADUATED);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        assertThrows(
                StudentStatusChangeNotAllowedException.class,
                () -> studentService.suspendStudent(studentId)
        );
    }

    @Test
    void shouldActivateStudent() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.SUSPENDED);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        studentService.activateStudent(studentId);

        assertEquals(
                StudentStatus.ACTIVE,
                student.getStatus()
        );
    }

    @Test
    void shouldThrowWhenStudentAlreadyActive() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.ACTIVE);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        assertThrows(
                StudentStatusChangeNotAllowedException.class,
                () -> studentService.activateStudent(studentId)
        );
    }

    @Test
    void shouldThrowWhenActivatingGraduatedStudent() {

        Integer studentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        student.setStatus(StudentStatus.GRADUATED);

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        assertThrows(
                StudentStatusChangeNotAllowedException.class,
                () -> studentService.activateStudent(studentId)
        );
    }

    @Test
    void shouldAssignDepartment() {

        Integer studentId = 1;
        Integer departmentId = 1;

        Student student = new Student(
                studentId,
                "Lazar",
                "lazar@mail.com",
                20
        );

        Department department =
                new Department();

        department.setId(departmentId);
        department.setName("Software Engineering");
        department.setCode("SE");

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(department));

        studentService.assignDepartment(
                studentId,
                departmentId
        );

        assertEquals(
                department,
                student.getDepartment()
        );
    }

    @Test
    void shouldThrowWhenAssigningDepartmentToNonExistingStudent() {

        Integer studentId = 1;
        Integer departmentId = 1;

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.assignDepartment(
                        studentId,
                        departmentId
                )
        );

        verify(departmentRepository, never())
                .findById(anyInt());
    }

    @Test
    void shouldThrowWhenDepartmentDoesNotExist() {

        Integer studentId = 1;
        Integer departmentId = 1;

        Student student =
                new Student(
                        studentId,
                        "Lazar",
                        "lazar@mail.com",
                        20
                );

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.empty());

        assertThrows(
                DepartmentNotFoundException.class,
                () -> studentService.assignDepartment(
                        studentId,
                        departmentId
                )
        );
    }

    @Test
    void shouldThrowWhenAssigningDepartmentToGraduatedStudent() {

        Integer studentId = 1;
        Integer departmentId = 1;

        Student student =
                new Student(
                        studentId,
                        "Lazar",
                        "lazar@mail.com",
                        20
                );

        student.setStatus(
                StudentStatus.GRADUATED
        );

        Department department =
                new Department();

        department.setId(departmentId);
        department.setName("Software Engineering");
        department.setCode("SE");

        when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));

        when(departmentRepository.findById(departmentId))
                .thenReturn(Optional.of(department));

        assertThrows(
                StudentStatusChangeNotAllowedException.class,
                () -> studentService.assignDepartment(
                        studentId,
                        departmentId
                )
        );
    }

    @Test
    void shouldAssignCourse() {

        Student student = new Student();
        student.setId(1);
        student.setStatus(StudentStatus.ACTIVE);
        Department department = new Department();
        department.setId(1);

        student.setDepartment(department);

        Course course = new Course();
        course.setId(1);
        course.setCredits(6);
        course.setDepartment(department);
        course.setStudents(new HashSet<>());
        course.setMaxStudents(30);

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1))
                .thenReturn(Optional.of(course));



        studentService.assignCourse(1, 1);

        assertTrue(student.getCourses().contains(course));
    }

    @Test
    void shouldThrowWhenStudentNotFoundDuringCourseAssignment() {

        when(studentRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.assignCourse(1, 1)
        );
    }

    @Test
    void shouldThrowWhenCourseNotFound() {

        Student student = new Student();
        student.setId(1);

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CourseNotFoundException.class,
                () -> studentService.assignCourse(1, 1)
        );
    }

    @Test
    void shouldThrowWhenCourseAlreadyAssigned() {

        Student student = new Student();
        student.setId(1);
        student.setStatus(StudentStatus.ACTIVE);

        Department department = new Department();
        department.setId(1);

        student.setDepartment(department);

        Course course = new Course();
        course.setId(1);
        course.setDepartment(department);
        course.setStudents(new HashSet<>());
        course.setMaxStudents(30);

        student.getCourses().add(course);

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(1))
                .thenReturn(Optional.of(course));

        assertThrows(
                CourseAlreadyAssignedException.class,
                () -> studentService.assignCourse(1, 1)
        );
    }

    @Test
    void shouldThrowWhenCreditLimitExceeded() {

        Student student = new Student();
        student.setId(1);
        student.setStatus(StudentStatus.ACTIVE);

        Department department = new Department();
        department.setId(1);

        student.setDepartment(department);

        Course existingCourse = new Course();
        existingCourse.setId(1);
        existingCourse.setCredits(55);


        student.getCourses().add(existingCourse);

        Course newCourse = new Course();
        newCourse.setId(2);
        newCourse.setCredits(10);
        newCourse.setDepartment(department);
        newCourse.setStudents(new HashSet<>());
        newCourse.setMaxStudents(30);

        when(studentRepository.findById(1))
                .thenReturn(Optional.of(student));

        when(courseRepository.findById(2))
                .thenReturn(Optional.of(newCourse));



        assertThrows(
                CreditLimitExceededException.class,
                () -> studentService.assignCourse(1, 2)
        );
    }

}
