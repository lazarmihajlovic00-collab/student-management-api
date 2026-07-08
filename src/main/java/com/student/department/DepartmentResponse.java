package com.student.department;

public class DepartmentResponse {
    private Integer id;
    private String code;
    private String name;

    public DepartmentResponse(Department department) {
        this.id = department.getId();
        this.code = department.getCode();
        this.name = department.getName();
    }

    public Integer getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
}