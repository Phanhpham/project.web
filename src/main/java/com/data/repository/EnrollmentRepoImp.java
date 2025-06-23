package com.data.repository;

import com.data.model.Course;
import com.data.model.Enrollment;
import com.data.model.Status;
import com.data.model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EnrollmentRepoImp implements EnrollmentRepo {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void registerCourse(Enrollment enrollment) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(enrollment);
            tx.commit();
        } finally {
            session.close();
        }
    }

    @Override
    public List<Enrollment> getEnrollmentById(int studentId) {
        Session session = sessionFactory.openSession();

        Query<Enrollment> query = session.createQuery("FROM Enrollment e where e.student.id = :studentId", Enrollment.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }

    @Override
    public List<Enrollment> getAllEnrollment(int studentId, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();

        Query<Enrollment> query = session.createQuery("FROM Enrollment e where e.student.id = :studentId", Enrollment.class);
        query.setParameter("studentId", studentId);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long countTotal(int studentId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Enrollment ", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public List<Enrollment> searchByName(int studentId, String name, int offset, int limit) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Enrollment e WHERE LOWER(e.course.name) LIKE CONCAT('%',:name, '%') and e.student.id = :studentId";
        Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
        query.setParameter("name", name);
        query.setParameter("studentId", studentId);
        query.setFirstResult((offset - 1) * limit);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long countPageByName(int studentId, String courseName) {
        Session session = sessionFactory.openSession();

        String hql = "SELECT COUNT(e) FROM Enrollment e WHERE LOWER(e.course.name) LIKE :name and e.student.id = :studentId";

        return (Long) session.createQuery(hql)
                .setParameter("name", "%" + courseName.toLowerCase() + "%")
                .setParameter("studentId", studentId)
                .getSingleResult();
    }

    @Override
    public List<Enrollment> sortStatus(int studentId, String sortType, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();

        String hql = "from Enrollment e where e.student.id = :studentId";

        if (sortType.equalsIgnoreCase("ASC")){
            hql += " order by e.status asc";
        }else if (sortType.equalsIgnoreCase("DESC")){
            hql += " order by e.status desc";
        }

        Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
        query.setParameter("studentId", studentId);

        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<Enrollment> enrollments = query.getResultList();

        return enrollments;
    }

    @Override
    public long countToTalEnrollments() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Enrollment ", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public List<Enrollment> getAllStudentOfCourse(int pageNo, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Enrollment> query = session.createQuery("FROM Enrollment ", Enrollment.class);
            query.setFirstResult((pageNo - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public long count() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Enrollment ", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public void confirmEnrollment(int enrollmentId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null){
                enrollment.setStatus(Status.CONFIRMED);
                session.update(enrollment);
                tx.commit();
            }
        }finally {
            session.close();
        }
    }

    @Override
    public void denyEnrollment(int enrollmentId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null){
                enrollment.setStatus(Status.DENIED);
                session.update(enrollment);
                tx.commit();
            }
        }finally {
            session.close();
        }
    }

    @Override
    public List<Enrollment> filterStatus(String status, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();
        try{
            Status status1 = Status.valueOf(status);
            String hql = " from Enrollment where status = :status";
            Query<Enrollment> query = session.createQuery(hql,Enrollment.class);
            query.setParameter("status",status1);
            query.setFirstResult((pageNo - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }finally {
            session.close();
        }
    }

    @Override
    public long countStatus(String status) {
        try (Session session = sessionFactory.openSession()) {
            Status status1 = Status.valueOf(status);
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Enrollment where status = :status", Long.class);

            query.setParameter("status",status1);
            return query.uniqueResult();
        }
    }

    @Override
    public List<Enrollment> searchByNameCourse(String name, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Enrollment e WHERE LOWER(e.course.name) LIKE CONCAT('%',:name, '%')";
        Query<Enrollment> query = session.createQuery(hql, Enrollment.class);
        query.setParameter("name", name);
        query.setFirstResult((pageNo - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public long countSearch(String name) {
        Session session = sessionFactory.openSession();

        String hql = "SELECT COUNT(e) FROM Enrollment e WHERE LOWER(e.course.name) LIKE :name";

        return (Long) session.createQuery(hql)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .uniqueResult();
    }

    @Override
    public void cancelRegisterCourse(int enrollmentId) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null){
                enrollment.setStatus(Status.CANCELED);
                session.update(enrollment);
                tx.commit();
            }
        }finally {
            session.close();
        }
    }
}