package com.data.repository;

import com.data.model.Course;

import java.util.List;

public interface CourseRepo {
    List<Course> getAll();
}
