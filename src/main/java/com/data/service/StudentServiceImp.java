package com.data.service;

import com.data.model.Student;
import com.data.model.TotalStudent;
import com.data.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StudentServiceImp implements StudentService {
    @Autowired
    private StudentRepo studentRepo;

    @Override
    public boolean existsByUsername(String username) {
        return studentRepo.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return studentRepo.existsByEmail(email);
    }

    @Override
    public void save(Student student) {
        studentRepo.save(student);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return studentRepo.existsByPhone(phone);
    }

    @Override
    public Student findByEmail(String email) {
        return studentRepo.findByEmail(email);
    }

    @Override
    public List<Student> getStudentByPage(int pageNo, int pageSize) {
        return studentRepo.getStudentByPage(pageNo, pageSize);
    }

    @Override
    public long countTotalStudent() {
        return studentRepo.countTotalStudent();
    }

    @Override
    public void blockUserById(int studentId) {
        studentRepo.blockUserById(studentId);
    }

    @Override
    public void unblockUserById(int studentId) {
        studentRepo.unblockUserById(studentId);
    }

    @Override
    public List<Student> searchByName(String keyword, int offset, int limit) {
        return studentRepo.searchByName(keyword,offset,limit);
    }

    @Override
    public long countSearch(String keyword) {
        return studentRepo.countSearch(keyword);
    }

    @Override
    public List<Student> sortById(String sortBy, int pageNo, int pageSize) {
        return studentRepo.sortById(sortBy,pageNo,pageSize);
    }

    @Override
    public List<Student> sortByName(String sortBy, int pageNo, int pageSize) {
        return studentRepo.sortByName(sortBy,pageNo,pageSize);
    }

    @Override
    public List<TotalStudent> getTotalStudentOfCourse() {
        return studentRepo.getTotalStudentOfCourse();
    }

    @Override
    public void updateProfile(Student student) {
        studentRepo.updateProfile(student);
    }

    @Override
    public Student findById(int id) {
        return studentRepo.findById(id);
    }

    @Override
    public Student findByPhone(String phone) {
        return studentRepo.findByPhone(phone);
    }

    @Override
    public void changePassword(int studentId, String newPassWord) {
        studentRepo.changePassword(studentId,newPassWord);
    }
}
