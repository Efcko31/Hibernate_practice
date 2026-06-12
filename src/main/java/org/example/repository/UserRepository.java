package org.example.repository;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class UserRepository {

    public void save(User user) {
        //Сессия - это аналог соединения с бд из пула. Какой-то процесс (условно)
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);//состояние MANAGED
            transaction.commit();// Коммитим в базу. Только здесь улетит INSERT
        } catch (Exception e) {
            System.out.println("Ошибка при сохранении" + e.getMessage());
        }
    }

    public Optional<User> findById(long id) {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
//        catch (Exception e) {
//            System.out.println(String.format("Ошибка при поиске по ID: %d \n", id));
//
//        }
    }
}
