package com.student.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentRequest {

    @NotBlank(message = "Naziv smera je obavezan")
    @Size(min = 3, max = 100, message = "Naziv smera mora imati između 3 i 100 karaktera")
    private String name;

    @NotBlank(message = "Kod smera je obavezan")
    @Size(min = 2, max = 10, message = "Kod smera mora imati između 2 i 10 karaktera")
    private String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}