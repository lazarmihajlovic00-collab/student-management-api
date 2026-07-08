package com.student.department;

import com.student.common.BaseEntity;
import com.student.course.Course;
import com.student.student.Student;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
@SQLDelete(sql = "UPDATE departments SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String code;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "department")
    private List<Course> courses = new ArrayList<>();

    public Department() {
    }

    public Department(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department department)) return false;

        return id != null && id.equals(department.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}