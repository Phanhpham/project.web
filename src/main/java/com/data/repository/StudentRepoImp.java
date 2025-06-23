package com.data.repository;

import com.data.model.Course;
import com.data.model.Student;
import com.data.model.TotalStudent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepoImp implements StudentRepo{
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public boolean existsByUsername(String username) {
        String hql = "SELECT COUNT(s) FROM Student s WHERE s.username = :username";
        try (Session session = sessionFactory.openSession()) {
            Long count = (Long) session.createQuery(hql)
                    .setParameter("username", username)
                    .uniqueResult();
            return count > 0;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        String hql = "SELECT COUNT(s) FROM Student s WHERE s.email = :email";
        try (Session session = sessionFactory.openSession()) {
            Long count = (Long) session.createQuery(hql)
                    .setParameter("email", email)
                    .uniqueResult();
            return count > 0;
        }
    }

    @Override
    public void save(Student student) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(student);
            tx.commit();
        }
    }

    @Override
    public boolean existsByPhone(String phone) {
        String hql = "SELECT COUNT(s) FROM Student s WHERE s.phone = :phone";
        try (Session session = sessionFactory.openSession()) {
            Long count = (Long) session.createQuery(hql)
                    .setParameter("phone", phone)
                    .uniqueResult();
            return count > 0;
        }
    }

    @Override
    public Student findByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Student WHERE email = :email";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public List<Student> getStudentByPage(int pageNo, int pageSize) {
        try (Session session = sessionFactory.openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            query.setFirstResult((pageNo - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.getResultList();
        }
    }

    @Override
    public long countTotalStudent() {
        try (Session session = sessionFactory.openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Student where role = true", Long.class);
            return query.uniqueResult();
        }
    }

    @Override
    public void blockUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                student.setStatus(false);
                session.update(student);
            }
            tx.commit();
        }
    }

    @Override
    public void unblockUserById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                student.setStatus(true);
                session.update(student);
            }
            tx.commit();
        }
    }


    @Override
    public List<Student> searchByName(String keyword, int pageNo, int pageSize) {
       Session session = sessionFactory.openSession();
       try{
           String hql = "FROM Student where name LIKE CONCAT('%', :keyword, '%' ) or " +
                   "email LIKE CONCAT('%', :keyword, '%' )";
           Integer id = null;
           try {
               id = Integer.parseInt(keyword);
               hql+= " or id = :id";
           }catch (NumberFormatException e){
               e.printStackTrace();
           }
           Query<Student> query = session.createQuery(hql, Student.class);
           query.setParameter("keyword",keyword);
           if (id!= null){
               query.setParameter("id",id);
           }
           query.setFirstResult((pageNo - 1) * pageSize);
           query.setMaxResults(pageSize);
           return query.getResultList();
       }finally{
           session.close();
       }
    }

    @Override
    public long countSearch(String keyword) {
        Session session = sessionFactory.openSession();
        try{
            String hql = "SELECT count(*)  FROM Student where name LIKE CONCAT('%', :keyword, '%' ) or " +
                    "email LIKE CONCAT('%', :keyword, '%' )";

            Integer id = null;
            try {
                id = Integer.parseInt(keyword);
                hql+= " or id = :id";
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
            Query<Long> query = session.createQuery(hql,Long.class);
            query.setParameter("keyword",keyword);
            if (id!= null){
                query.setParameter("id",id);
            }
            return query.getSingleResult();
        }finally {
            session.close();
        }
    }

    @Override
    public List<Student> sortById(String sortBy, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Student";
        if (sortBy.equals("ASC")) {
            hql += " order by id";

        } else if (sortBy.equals("DESC")) {
            hql += " order by id desc";
        }
        Query<Student> studentQuery = session.createQuery(hql, Student.class);
        studentQuery.setFirstResult((pageNo - 1) * pageSize);
        studentQuery.setMaxResults(pageSize);
        return studentQuery.getResultList();
    }

    @Override
    public List<Student> sortByName(String sortBy, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Student";
        if (sortBy.equals("ASC")) {
            hql += " order by name";

        } else if (sortBy.equals("DESC")) {
            hql += " order by name desc";
        }
        Query<Student> studentQuery = session.createQuery(hql, Student.class);
        studentQuery.setFirstResult((pageNo - 1) * pageSize);
        studentQuery.setMaxResults(pageSize);
        return studentQuery.getResultList();
    }

    @Override
    public List<TotalStudent> getTotalStudentOfCourse() {
        Session session = sessionFactory.openSession();

        String hql = "select new com.data.model.TotalStudent(" +
                "c.id, c.image, c.name, c.duration , count(e.student.id)) " +
                "from com.data.model.Course c " +
                "left join com.data.model.Enrollment e ON e.course.id = c.id and e.status = 'CONFIRMED' " +
                "group by c.id, c.image, c.name, c.duration " +
                "order by count(e.student.id) desc ";

        Query<TotalStudent> query = session.createQuery(hql, TotalStudent.class);
        return query.getResultList();
    }

    @Override
    public void updateProfile(Student student) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            session.update(student);
            tx.commit();
        }finally {
            session.close();
        }
    }

    @Override
    public Student findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Student WHERE id = :id";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("id", id);
        return query.uniqueResult();
    }

    @Override
    public Student findByPhone(String phone) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "FROM Student WHERE phone = :phone";
        Query<Student> query = session.createQuery(hql, Student.class);
        query.setParameter("phone", phone);
        return query.uniqueResult();
    }

    @Override
    public void changePassword(int studentId, String passWord) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Student student = findById(studentId);
            if (student != null) {
                student.setPassword(passWord);
                session.update(student);
                transaction.commit();
            }
        }finally {
            session.close();
        }
    }
}
