package com.data.service;

import com.data.model.Student;

public interface StudentService {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void save(Student student);
    boolean existsByPhone(String phone);
    Student findByEmail(String email);
}
