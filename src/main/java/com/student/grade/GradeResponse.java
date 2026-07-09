package com.student.grade;

import java.time.LocalDate;

public class GradeResponse {

    Integer id;
    Integer value;
    LocalDate examDate;
    String studentName;
    String courseName;

    public GradeResponse(Integer id, Integer value, LocalDate examDate, String studentName, String courseName) {
        this.id = id;
        this.value = value;
        this.examDate = examDate;
        this.studentName = studentName;
        this.courseName = courseName;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }
    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

}
