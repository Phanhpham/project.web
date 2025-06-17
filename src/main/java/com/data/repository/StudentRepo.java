package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;

import java.util.List;

public interface StudentRepo {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void save(Student student);
    Student findByEmail(String email);
    public List<Student> getStudentByPage(int pageNo, int pageSize);
    public long countTotalStudent();
    void blockUserById(int studentId);
    void unblockUserById(int studentId);
//    List<Student> searchByName(String name, String email, int id, int offset, int limit);
}
