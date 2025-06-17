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

public class CourseRepoImp implements CourseRepo {
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Course> getAll() {
        Session session = sessionFactory.openSession();
        Query<Course> query = session.createQuery(" from Course", Course.class);
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
    public boolean existsByCourseNameIgnoreCase(String name, int id) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT COUNT(s) FROM Course s WHERE LOWER(s.name) = :name";
            if (id > 0) {
                hql += " and id != :id";
            }
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("name", name);
            if (id > 0) {
                query.setParameter("id", id);
            }
            Long count = query.getSingleResult();
            return count > 0;
        }
    }

    @Override
    public void save(Course course) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(course);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Course findById(int id) {
        Session session = sessionFactory.openSession();
        Query<Course> courseQuery = session.createQuery("from Course where id=:id", Course.class);
        courseQuery.setParameter("id", id);
        return courseQuery.getSingleResult();
    }

    @Override
    public void deleteById(int id) {
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                session.delete(course);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> searchByName(String name, int offset, int limit) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Course c WHERE LOWER(c.name) LIKE CONCAT('%',:name, '%')";
        Query<Course> query = session.createQuery(hql, Course.class);
        query.setParameter("name", name);
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

    @Override
    public List<Course> sortByName(String action, int pageNo, int pageSize) {
        Session session = sessionFactory.openSession();

        String hql = "FROM Course";
        if (action.equals("asc")) {
            hql += " order by name";

        } else if (action.equals("desc")) {
            hql += " order by name desc";
        }
        Query<Course> courseQuery = session.createQuery(hql, Course.class);
        courseQuery.setFirstResult((pageNo - 1) * pageSize);
        courseQuery.setMaxResults(pageNo);
        return courseQuery.getResultList();
    }

    public boolean checkCourseNameExisted(String courseName) {
        Session session = sessionFactory.openSession();
        try {
            return session.createQuery("select count(c.id) from Course c where c.name = :name", Long.class)
                    .setParameter("name", courseName).getSingleResult() > 0;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public List<Course> findAll(int page, int pageSize, String keyword, String sortBy, String sort) {
        int offset = (page - 1) * pageSize;

        // Sử dụng StringBuilder để xây dựng chuỗi truy vấn
        StringBuilder sql = new StringBuilder("FROM Course c WHERE 1=1");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND c.name LIKE :keyword");
        }

        if ("name".equals(sortBy)) {
            sql.append(" ORDER BY c.name ").append(sort);
        } else {
            sql.append(" ORDER BY c.id ").append(sort);
        }

        Session session = sessionFactory.openSession();
        try {
            // Tạo truy vấn
            Query query = session.createQuery(sql.toString(), Course.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize);

            // Thiết lập tham số nếu keyword có giá trị
            if (keyword != null && !keyword.isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }

            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();  // In ra lỗi để dễ dàng kiểm tra
            return null;
        } finally {
            session.close();  // Đảm bảo đóng phiên làm việc
        }
    }
}
