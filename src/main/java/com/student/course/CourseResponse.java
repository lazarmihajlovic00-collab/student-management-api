package com.student.course;

public class CourseResponse {
    private Integer id;
    private String code;
    private String name;
    private Integer credits;
    private Integer maxStudents;

    public CourseResponse(Course course) {
        this.id = course.getId();
        this.code = course.getCode();
        this.name = course.getName();
        this.credits = course.getCredits();
        this.maxStudents = course.getMaxStudents();
    }


    public Integer getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public Integer getCredits() { return credits; }
    public Integer getMaxStudents() { return maxStudents; }
}