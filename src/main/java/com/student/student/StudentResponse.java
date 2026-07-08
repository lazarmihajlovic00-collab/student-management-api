package com.student.student;

import com.student.course.CourseResponse;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentResponse {
    private Integer id;
    private String name;
    private String email;
    private Integer age;
    private StudentStatus status;
    private LocalDate enrollmentDate;
    private LocalDate graduationDate;
    private String departmentName;
    private Set<CourseResponse> courses;

    public StudentResponse(Student student) {
        this.id = student.getId();
        this.name = student.getName();
        this.email = student.getEmail();
        this.age = student.getAge();
        this.status = student.getStatus();
        this.enrollmentDate = student.getEnrollmentDate();
        this.graduationDate = student.getGraduationDate();
        this.departmentName = student.getDepartment() != null ? student.getDepartment().getName() : null;
        this.courses = student.getCourses() != null ?
                student.getCourses().stream().map(CourseResponse::new).collect(Collectors.toSet()) : null;
    }


    public Integer getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Integer getAge() { return age; }
    public StudentStatus getStatus() { return status; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public LocalDate getGraduationDate() { return graduationDate; }
    public String getDepartmentName() { return departmentName; }
    public Set<CourseResponse> getCourses() { return courses; }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public void setGraduationDate(LocalDate graduationDate) {
        this.graduationDate = graduationDate;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public void setCourses(Set<CourseResponse> courses) {
        this.courses = courses;
    }
}