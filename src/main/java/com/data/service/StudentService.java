package com.data.service;

import com.data.model.Student;

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

//    List<Student> searchByName(String name, String email, int id, int offset, int limit);
}
