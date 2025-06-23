package com.data.service;

import com.data.model.Enrollment;
import com.data.repository.EnrollmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentServiceImp implements EnrollmentService {
    @Autowired
    private EnrollmentRepo enrollmentRepo;

    @Override
    public void registerCourse(Enrollment enrollment) {
        enrollmentRepo.registerCourse(enrollment);
    }

    @Override
    public List<Enrollment> getEnrollmentById(int studentId) {
        return enrollmentRepo.getEnrollmentById(studentId);
    }

    @Override
    public List<Enrollment> getAllEnrollment(int studentId, int pageNo, int pageSize) {
        return enrollmentRepo.getAllEnrollment(studentId,pageNo,pageSize);
    }

    @Override
    public long countTotal(int studentId) {
        return enrollmentRepo.countTotal(studentId);
    }

    @Override
    public List<Enrollment> searchByName(int studentId, String name, int offset, int limit) {
        return enrollmentRepo.searchByName(studentId,name,offset,limit);
    }

    @Override
    public long countPageByName(int studentId, String courseName) {
        return enrollmentRepo.countPageByName(studentId,courseName);
    }

    @Override
    public List<Enrollment> sortStatus(int studentId, String sortType, int pageNo, int pageSize) {
        return enrollmentRepo.sortStatus(studentId,sortType,pageNo,pageSize);
    }

    @Override
    public long countToTalEnrollments() {
        return enrollmentRepo.countToTalEnrollments();
    }

    @Override
    public List<Enrollment> getAllStudentOfCourse(int pageNo, int pageSize) {
        return enrollmentRepo.getAllStudentOfCourse(pageNo,pageSize);
    }

    @Override
    public long count() {
        return enrollmentRepo.count();
    }

    @Override
    public void confirmEnrollment(int enrollmentId) {
        enrollmentRepo.confirmEnrollment(enrollmentId);
    }

    @Override
    public void denyEnrollment(int enrollmentId) {
        enrollmentRepo.denyEnrollment(enrollmentId);
    }

    @Override
    public List<Enrollment> filterStatus(String status, int pageNo, int pageSize) {
        return enrollmentRepo.filterStatus(status,pageNo,pageSize);
    }

    @Override
    public long countStatus(String status) {
        return enrollmentRepo.countStatus(status);
    }

    @Override
    public List<Enrollment> searchByNameCourse(String name, int pageNo, int pageSize) {
        return enrollmentRepo.searchByNameCourse(name,pageNo,pageSize);
    }

    @Override
    public long countSearch(String name) {
        return enrollmentRepo.countSearch(name);
    }

    @Override
    public void cancelRegisterCourse(int enrollmentId) {
        enrollmentRepo.cancelRegisterCourse(enrollmentId);
    }
}
