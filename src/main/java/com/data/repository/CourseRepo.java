package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;

import java.util.List;

public interface CourseRepo {
    List<Course> getAll();
    boolean existsByCourseNameIgnoreCase(String name,int id);
    void save(Course course);
    public List<Course> getCourseByPage(int pageNo, int pageSize);
    public long countTotalCourse();
    Course findById(int id);
    void deleteById(int id);
    List<Course> searchByName(String name, int offset, int limit);
    long countByName(String name);
    List<Course> sortByName(String action,int pageNo, int pageSize);
    boolean checkCourseNameExisted (String courseName);
    List<Course> findAll(int page,int pageSize,String keyword,String sortBy, String sort);
}
