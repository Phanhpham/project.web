package com.data.repository;

import com.data.model.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepoImp implements CourseRepo{
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Course> getAll() {
        Session session = sessionFactory.openSession();
        Query<Course> query = session.createQuery(" from Course",Course.class);
        return query.getResultList();
    }
}
