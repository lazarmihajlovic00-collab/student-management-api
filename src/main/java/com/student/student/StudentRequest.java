package com.student.student;


import jakarta.validation.constraints.*;

public class StudentRequest {

    @NotBlank(message = "Ime studenta je obavezno")
    @Size(min = 2, max = 100, message = "Ime mora imati između 2 i 100 karaktera")
    private String name;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email adresa mora biti u ispravnom formatu")
    private String email;

    @NotNull(message = "Godine su obavezne")
    @Min(value = 16, message = "Student ne može biti mlađi od 16 godina")
    @Max(value = 100, message = "Nevalidan broj godina")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
