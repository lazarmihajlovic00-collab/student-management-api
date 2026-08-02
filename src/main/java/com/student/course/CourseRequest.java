package com.student.course;

import jakarta.validation.constraints.*;

public class CourseRequest {

    @NotBlank(message = "Naziv kursa je obavezan")
    @Size(min = 2, max = 100, message = "Naziv kursa mora imati između 2 i 100 karaktera")
    private String name;

    @NotBlank(message = "Kod kursa je obavezan")
    @Size(min = 2, max = 10, message = "Kod kursa mora imati između 2 i 10 karaktera")
    private String code;

    @NotNull(message = "Broj ESPB bodova je obavezan")
    @Min(value = 1, message = "Kurs mora nositi najmanje 1 ESPB bod")
    @Max(value = 30, message = "Kurs ne može nositi više od 30 ESPB bodova")
    private Integer credits;

    @NotNull(message = "Maksimalan broj studenata je obavezan")
    @Min(value = 5, message = "Minimalan kapacitet kursa je 5 studenata")
    @Max(value = 300, message = "Maksimalan kapacitet kursa je 300 studenata")
    private Integer maxStudents;

    @NotNull(message = "Smer je obavezan")
    private Integer departmentId;

    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

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

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }
}
