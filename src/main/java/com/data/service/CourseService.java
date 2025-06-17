package com.data.service;

import com.data.model.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAll();
    public List<Course> getCourseByPage(int pageNo, int pageSize);
    public long countTotalCourse();
    boolean existsByCourseNameIgnoreCase(String name, int id);
    void save(Course course);
    Course findById(int id);
    void deleteById(int id);
    List<Course> searchByName(String name, int offset, int limit);
    long countByName(String name);
    List<Course> sortByName(String action,int pageNo, int pageSize);
    boolean checkCourseNameExisted(String name);
    List<Course> findAll(int page,int pageSize,String keyword,String sortBy, String sort);
}
