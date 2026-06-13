package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Override
    public void save(User user) {
        log.info("DAO: Начало сохранения пользователя: {}", user.getUsername());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            log.info("DAO: Пользователь {} успешно сохранен с id={}", user.getUsername(), user.getId());
        } catch (Exception e) {
            log.error("DAO: Ошибка при сохранении пользователя {}", user.getUsername(), e);
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("DAO: Поиск пользователя по id={}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        } catch (Exception e) {
            log.error("DAO: Ошибка при поиске пользователя по id={}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByIdWithBasket(Long id) {
        log.info("DAO: Поиск пользователя по id={} с загрузкой корзины (JOIN FETCH)", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT u FROM User u LEFT JOIN FETCH u.productBasket WHERE u.id = :id";
            User user = session.createQuery(hql, User.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("DAO: Ошибка JOIN FETCH запроса для пользователя id={}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        log.info("DAO: Запрос списка всех пользователей");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    @Override
    public void update(User user) {
        log.info("DAO: Обновление пользователя с id={}", user.getId());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user);
            tx.commit();
            log.info("DAO: Данные пользователя с id={} успешно обновлены", user.getId());
        } catch (Exception e) {
            log.error("DAO: Ошибка при обновлении пользователя id={}", user.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        log.info("DAO: Удаление пользователя по id={}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                log.info("DAO: Пользователь с id={} успешно удален", id);
            } else {
                log.warn("DAO: Пользователь с id={} не найден для удаления", id);
            }
            tx.commit();
        } catch (Exception e) {
            log.error("DAO: Ошибка при удалении пользователя id={}", id, e);
        }
    }
}