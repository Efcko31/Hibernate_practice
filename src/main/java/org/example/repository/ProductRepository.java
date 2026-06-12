package org.example.repository;

import org.example.entity.Product;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ProductRepository {
    public void save(Product product) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();
        }
    }

    public Optional<Product> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Product.class, id));
        }
    }

    public List<Product> findAll() {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Product", Product.class).list();
        }
    }

    public void update(Product product) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();
        }
    }

    public void deleteById(Long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User deleteUser = session.get(User.class, id);

            if(deleteUser != null) {
                session.remove(deleteUser);
            }
            transaction.commit();
        }
    }
}
