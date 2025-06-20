package com.data.service;

import com.data.model.Course;
import com.data.repository.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImp implements CourseService{
    @Autowired
    private CourseRepo courseRepo;
    @Override
    public List<Course> getAll() {
        return courseRepo.getAll();
    }

    @Override
    public List<Course> getCourseByPage(int pageNo, int pageSize) {
        return courseRepo.getCourseByPage(pageNo,pageSize);
    }

    @Override
    public long countTotalCourse() {
        return courseRepo.countTotalCourse();
    }

    @Override
    public boolean existsByCourseNameIgnoreCase(String name, int id) {
        return courseRepo.existsByCourseNameIgnoreCase(name, id);
    }

    @Override
    public void save(Course course) {
        courseRepo.save(course);
    }

    @Override
    public Course findById(int id) {
        return courseRepo.findById(id);
    }

    @Override
    public void deleteById(int id) {
        courseRepo.deleteById(id);
    }

    @Override
    public List<Course> searchByName(String name, int offset, int limit) {
        return courseRepo.searchByName(name,offset,limit);
    }

    @Override
    public long countByName(String name) {
        return courseRepo.countByName(name);
    }

    @Override
    public List<Course> sortByName(String action, int pageNo, int pageSize) {
        return courseRepo.sortByName(action,pageNo,pageSize);
    }

    public boolean checkCourseNameExisted(String name){
        return courseRepo.checkCourseNameExisted(name);
    }

    @Override
    public List<Course> findAll(int page, int pageSize, String keyword, String sortBy, String sort) {
        return courseRepo.findAll(page,pageSize,keyword,sortBy,sort);
    }
}
