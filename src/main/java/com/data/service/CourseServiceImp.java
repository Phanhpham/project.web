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
}
