package org.example.repository;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Override
    public void save(UserEntity userEntity) {
        log.info("DAO: Начало сохранения пользователя: {}", userEntity.getUsername());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(userEntity);
            tx.commit();
            log.info("DAO: Пользователь {} успешно сохранен с id={}", userEntity.getUsername(), userEntity.getId());
        } catch (Exception e) {
            log.error("DAO: Ошибка при сохранении пользователя {}", userEntity.getUsername(), e);
        }
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        log.info("DAO: Поиск пользователя по id={}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(UserEntity.class, id));
        } catch (Exception e) {
            log.error("DAO: Ошибка при поиске пользователя по id={}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findByIdWithBasket(Long id) {
        log.info("DAO: Поиск пользователя по id={} с загрузкой корзины (JOIN FETCH)", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT u FROM User u LEFT JOIN FETCH u.productBasket WHERE u.id = :id";
            UserEntity userEntity = session.createQuery(hql, UserEntity.class)
                    .setParameter("id", id)
                    .uniqueResult();
            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            log.error("DAO: Ошибка JOIN FETCH запроса для пользователя id={}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public List<UserEntity> findAll() {
        log.info("DAO: Запрос списка всех пользователей");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", UserEntity.class).list();
        }
    }

    @Override
    public void update(UserEntity userEntity) {
        log.info("DAO: Обновление пользователя с id={}", userEntity.getId());
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(userEntity);
            tx.commit();
            log.info("DAO: Данные пользователя с id={} успешно обновлены", userEntity.getId());
        } catch (Exception e) {
            log.error("DAO: Ошибка при обновлении пользователя id={}", userEntity.getId(), e);
        }
    }

    @Override
    public void deleteById(Long id) {
        log.info("DAO: Удаление пользователя по id={}", id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            UserEntity userEntity = session.get(UserEntity.class, id);
            if (userEntity != null) {
                session.remove(userEntity);
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