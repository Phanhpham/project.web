package com.data.service;

import com.data.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAll();
    public List<Course> getCourseByPage(int pageNo, int pageSize);
    public long countTotalCourse();
    boolean existsByCourseNameIgnoreCase(String name);
    void save(Course course);
    Course findById(int id);
    void deleteById(int id);
    List<Course> searchByName(String name, int offset, int limit);
    long countByName(String name);
}
