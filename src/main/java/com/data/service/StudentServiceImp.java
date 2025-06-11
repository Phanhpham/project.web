package com.data.service;

import com.data.model.Student;
import com.data.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
