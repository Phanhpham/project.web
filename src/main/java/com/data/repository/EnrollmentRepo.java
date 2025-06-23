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
    long countToTalEnrollments();
    List<Enrollment> sortStatus(int studentId,String sortType,int pageNo,int pageSize);

    List<Enrollment> getAllStudentOfCourse(int pageNo,int pageSize);
    long count();
    void confirmEnrollment(int enrollmentId);
    void denyEnrollment(int enrollmentId);
    List<Enrollment> filterStatus(String status, int pageNo, int pageSize);
    long countStatus(String status);
    List<Enrollment> searchByNameCourse(String name,int pageNo, int pageSize);
    long countSearch(String name);

    void cancelRegisterCourse(int enrollmentId);
}
