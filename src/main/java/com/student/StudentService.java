package com.student;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getAllStudents(Pageable pageable){
        return studentRepository.findAll(pageable);
    }

    public Student getStudentById(Integer id){
        return studentRepository.findById(id).orElseThrow(() -> new IllegalStateException(id + " not found"));
    }

    public void deleteStudent(Integer id){
        studentRepository.deleteById(id);
    }

    public void addStudentRequest(StudentRequest request){
        if(studentRepository.existsByEmail(request.getEmail())){
            throw new IllegalStateException("Email already taken");
        }

        Student student = new Student();
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());

        studentRepository.save(student);

    }

    @Transactional
    public void updateStudentRequest(Integer id, StudentRequest request){
        Student student = studentRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Student not found"));

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

}
