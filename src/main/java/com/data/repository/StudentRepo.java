package com.data.repository;

import com.data.model.Student;

public interface StudentRepo {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void save(Student student);
    Student findByEmail(String email);
}
