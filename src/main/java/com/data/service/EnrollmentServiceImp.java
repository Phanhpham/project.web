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
}
