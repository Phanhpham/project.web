package com.data.service;

import com.data.model.Student;
import com.data.model.TotalStudent;

import java.util.List;

public interface StudentService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void save(Student student);
    boolean existsByPhone(String phone);
    Student findByEmail(String email);

    public List<Student> getStudentByPage(int pageNo, int pageSize);
    public long countTotalStudent();

    void blockUserById(int studentId);
    void unblockUserById(int studentId);

    List<Student> searchByName(String keyword, int offset, int limit);

    long countSearch(String keyword);

    List<Student> sortById(String sortBy, int pageNo, int pageSize);
    List<Student> sortByName(String sortBy, int pageNo, int pageSize);
    List<TotalStudent> getTotalStudentOfCourse();
    void updateProfile(Student student);
    Student findById(int id);
    Student findByPhone(String phone);
    void changePassword(int studentId,String newPassWord);

}
