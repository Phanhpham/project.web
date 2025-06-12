package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;

import java.util.List;

public interface CourseRepo {
    List<Course> getAll();
    boolean existsByCourseNameIgnoreCase(String name);
    void save(Course course);
    public List<Course> getCourseByPage(int pageNo, int pageSize);
    public long countTotalCourse();
    Course findById(int id);
    void deleteById(int id);
    List<Course> searchByName(String name, int offset, int limit);
    long countByName(String name);

}
