package com.data.repository;

import com.data.model.Course;
import com.data.model.Enrollment;

import java.util.List;

public interface EnrollmentRepo {
    void registerCourse(Enrollment enrollment);

    List<Enrollment> getEnrollmentById(int studentId);
    List<Enrollment> getAllEnrollment(int studentId,int pageNo,int pageSize);
     long countTotal(int studentId);
    List<Enrollment> searchByName(int studentId,String name, int offset, int limit);
    long countPageByName(int studentId, String courseName);

    List<Enrollment> sortStatus(int studentId,String sortType,int pageNo,int pageSize);
}
