package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
    @Override
    public List<Course> getCourseByPage(int pageNo, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("FROM Course", Course.class);
            query.setFirstResult((pageNo - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public long countTotalCourse() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Course", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public boolean existsByCourseNameIgnoreCase(String name) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(s) FROM Course s WHERE LOWER(s.name) = :name";
            Long count = session.createQuery(hql, Long.class)
                    .setParameter("name", name.toLowerCase())
                    .uniqueResult();
            return count > 0;
        }
    }
    @Override
    public void save(Course course) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx= session.beginTransaction();
            session.saveOrUpdate(course);
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Course findById(int id) {
        Session session = sessionFactory.openSession();
        Query<Course> courseQuery = session.createQuery("from Course where id=:id",Course.class);
        courseQuery.setParameter("id", id);
        return courseQuery.getSingleResult();
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if(course != null) {
                session.delete(course);
            }
            tx.commit();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public List<Course> searchByName(String name, int offset, int limit) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Course c WHERE LOWER(c.name) LIKE CONCAT('%',:name, '%')";
        Query<Course> query = session.createQuery(hql, Course.class);
        query.setParameter("name",name);
        query.setFirstResult((offset - 1) * limit);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long countByName(String name) {
        Session session = sessionFactory.openSession();

        String hql = "SELECT COUNT(c) FROM Course c WHERE LOWER(c.name) LIKE :name";

        return (Long) session.createQuery(hql)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .uniqueResult();
    }

}
