package com.student.student;

import com.student.course.Course;
import com.student.course.CourseRepository;
import com.student.department.Department;
import com.student.department.DepartmentRepository;
import com.student.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.List;

@Service
public class StudentService {

    private static final int REQUIRED_CREDITS_FOR_GRADUATION = 180;
    private static final int MAX_SEMESTER_CREDITS = 60;

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository, DepartmentRepository departmentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
    }

    public Page<Student> getAllStudents(Pageable pageable){
        return studentRepository.findAll(pageable);
    }

    public Student getStudentById(Integer id){
        return studentRepository.findById(id).orElseThrow(() ->
                new StudentNotFoundException("Student with id " + id + " not found"));
    }

    public void deleteStudent(Integer id){
        if(!studentRepository.existsById(id)){
            throw new StudentNotFoundException(
                    "Student with id " + id + " not found"
            );
        }
        studentRepository.deleteById(id);
    }

    public void addStudentRequest(StudentRequest request){
        if(studentRepository.existsByEmail(request.getEmail())){
            throw new EmailAlreadyExistsException("Email already taken");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());

        student.setStatus(StudentStatus.ACTIVE);
        student.setEnrollmentDate(java.time.LocalDate.now());
        student.setGraduationDate(null);

        studentRepository.save(student);

    }

    @Transactional
    public void updateStudentRequest(Integer id, StudentRequest request){

        Student student = studentRepository.findById(id).orElseThrow(
                () -> new StudentNotFoundException(
                        "Student with id " + id + " not found"
                ));

        if (!student.getEmail().equals(request.getEmail())
                && studentRepository.existsByEmail(request.getEmail())) {

            throw new EmailAlreadyExistsException(
                    "Email already taken"
            );
        }

        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());
    }

    public List<Student> searchStudentsByName(String name){
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Student> searchStudentsByEmail(String email){
        return studentRepository.findByEmailContainingIgnoreCase(email);
    }

    @Transactional
    public void graduateStudent(Integer id) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with id " + id + " not found"
                        )
                );

        if (student.getStatus() == StudentStatus.GRADUATED) {
            throw new StudentAlreadyGraduatedException(
                    "Student has already graduated"
            );
        }

        if (student.getStatus() == StudentStatus.SUSPENDED) {
            throw new StudentStatusChangeNotAllowedException(
                    "Suspended student cannot graduate"
            );
        }

        int totalCredits = student.getCourses()
                .stream()
                .mapToInt(Course::getCredits)
                .sum();

        if (totalCredits < REQUIRED_CREDITS_FOR_GRADUATION) {
            throw new StudentStatusChangeNotAllowedException(
                    "Student does not have enough credits to graduate"
            );
        }

        student.setStatus(StudentStatus.GRADUATED);
        student.setGraduationDate(LocalDate.now());
    }

    @Transactional
    public void suspendStudent(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with id " + id + " not found"
                        )
                );
        if (student.getStatus() == StudentStatus.SUSPENDED) {

            throw new StudentStatusChangeNotAllowedException(
                    "Student with id " + id + " is already suspended"
            );
        }
        if (student.getStatus() == StudentStatus.GRADUATED) {

            throw new StudentStatusChangeNotAllowedException(
                    "Graduated student cannot be suspended"
            );
        }
        student.setStatus(StudentStatus.SUSPENDED);
    }

    @Transactional
    public void activateStudent(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with id " + id + " not found"
                        )
                );
        if (student.getStatus() == StudentStatus.ACTIVE) {

            throw new StudentStatusChangeNotAllowedException(
                    "Student is already active"
            );
        }
        if (student.getStatus() == StudentStatus.GRADUATED) {

            throw new StudentStatusChangeNotAllowedException(
                    "Graduated student cannot be activated"
            );
        }
        student.setStatus(StudentStatus.ACTIVE);
    }

    @Transactional
    public void assignDepartment(
            Integer studentId,
            Integer departmentId
    ) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with id "
                                        + studentId
                                        + " not found"
                        )
                );

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() ->
                        new DepartmentNotFoundException(
                                "Department with id "
                                        + departmentId
                                        + " not found"
                        )
                );

        if (student.getStatus() == StudentStatus.GRADUATED) {
            throw new StudentStatusChangeNotAllowedException(
                    "Graduated students cannot change department"
            );
        }

        if (student.getDepartment() != null
                && student.getDepartment().getId().equals(departmentId)) {

            throw new StudentStatusChangeNotAllowedException(
                    "Student is already assigned to this department"
            );
        }

        if (student.getDepartment() != null) {
            student.getDepartment().getStudents().remove(student);
        }

        student.setDepartment(department);

        department.getStudents().add(student);
    }

    @Transactional
    public void assignCourse(
            Integer studentId,
            Integer courseId
    ) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new StudentNotFoundException(
                                "Student with id "
                                        + studentId
                                        + " not found"
                        )
                );

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new CourseNotFoundException(
                                "Course with id "
                                        + courseId
                                        + " not found"
                        )
                );

        if (student.getStatus() == StudentStatus.GRADUATED) {
            throw new CourseAssignmentNotAllowedException(
                    "Graduated students cannot enroll courses"
            );
        }

        if (student.getDepartment() == null) {
            throw new CourseAssignmentNotAllowedException(
                    "Student must be assigned to a department first"
            );
        }

        if (course.getDepartment() == null) {
            throw new CourseAssignmentNotAllowedException(
                    "Course is not assigned to any department"
            );
        }

        if (!course.getDepartment().getId()
                .equals(student.getDepartment().getId())) {

            throw new CourseAssignmentNotAllowedException(
                    "Course does not belong to student's department"
            );
        }

        if (student.getCourses().contains(course)) {
            throw new CourseAlreadyAssignedException(
                    "Course already assigned"
            );
        }

        if (course.getStudents().size() >= course.getMaxStudents()) {
            throw new CourseAssignmentNotAllowedException(
                    "Course is full"
            );
        }

        int totalCredits = student.getCourses()
                .stream()
                .mapToInt(Course::getCredits)
                .sum();

        if (totalCredits + course.getCredits() > MAX_SEMESTER_CREDITS) {
            throw new CreditLimitExceededException(
                    "Maximum credit limit exceeded"
            );
        }

        student.getCourses().add(course);

        if (!course.getStudents().contains(student)) {
            course.getStudents().add(student);
        }
    }

}
