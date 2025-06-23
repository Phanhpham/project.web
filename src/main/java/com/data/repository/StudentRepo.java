package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;
import com.data.model.TotalStudent;

import java.util.List;

public interface StudentRepo {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void save(Student student);
    Student findByEmail(String email);
    Student findByPhone(String phone);
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
    void changePassword(int studentId,String newPassWord);

}
