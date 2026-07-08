package com.student;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableJpaAuditing
public class StudentManagementApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApiApplication.class, args);
    }

    @GetMapping("v1")
    public String helloWorld(){
        return "Students management api";
    }

}
