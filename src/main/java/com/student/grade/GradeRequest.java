package com.student.grade;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class GradeRequest {

    @NotNull(message = "ID studenta je obavezan")
    private Integer studentId;

    @NotNull(message = "ID kursa je obavezan")
    private Integer courseId;

    @NotNull(message = "Ocena je obavezna")
    @Min(value = 5, message = "Minimalna ocena je 5")
    @Max(value = 10, message = "Maksimalna ocena je 10")
    private Integer value;

    @NotNull(message = "Datum ispita je obavezan")
    @PastOrPresent(message = "Datum ispita ne može biti u budućnosti")
    private LocalDate examDate;

    public GradeRequest() {}

    public GradeRequest(Integer studentId, Integer courseId, Integer value, LocalDate examDate) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.value = value;
        this.examDate = examDate;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
}