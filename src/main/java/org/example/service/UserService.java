package org.example.service;

import org.example.entity.BasketItemEntity;
import org.example.entity.ProductEntity;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;import java.util.Scanner;

public class UserService {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserRepository userRepository = new UserRepositoryImpl();

    public void addProductToUserBasket(Long userId, Long productId, Integer quantity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            UserEntity userEntity = session.get(UserEntity.class, userId);
            ProductEntity productEntity = session.get(ProductEntity.class, productId);

            if(userEntity != null && productEntity != null) {
                BasketItemEntity basketItemEntity = new BasketItemEntity();
                basketItemEntity.setUserEntity(userEntity);
                basketItemEntity.setProductEntity(productEntity);
                basketItemEntity.setQuantity(quantity);
                userEntity.addBasketItem(basketItemEntity);

                session.persist(basketItemEntity);
            }
            transaction.commit();
            System.out.println("товар добавлен в корзину");
        } catch (Exception e) {
            System.out.println("Не добавлен товар в корзину, причина: " + e.getMessage());
        }
    }

    public void create(String username, String pass) {
        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setUsername(username);
        newUserEntity.setPassword(pass);
        userRepository.save(newUserEntity);
    }

    public void showAllUsers() {
        userRepository.findAll().forEach(user ->
                System.out.printf("ID: %d | Логин: %s \n",
                        user.getId(), user.getUsername())
        );
    }

    public void showUserBasket(Long id) {
        userRepository.findByIdWithBasket(id).ifPresentOrElse(user -> {
            System.out.printf("Корзина пользователя %s:\n", user.getUsername());
            if (user.getProductBasket() == null || user.getProductBasket().isEmpty()) {
                System.out.println("  [Корзина пуста]");
            } else {
                user.getProductBasket().forEach(item ->
                        System.out.printf("  - %s | Количество: %d шт.\n",
                                item.getProductEntity().getName(), item.getQuantity())
                );
            }
        }, () -> System.out.println("Пользователь не найден!"));
    }

    public void updateUser(UserEntity updateData) {
        userRepository.findById(updateData.getId()).ifPresentOrElse(user -> {
            System.out.println(String.format("Приветствую %s !", user.getUsername()));

            userRepository.update(updateData);
        }, () -> System.out.println("Пользователь не найден!"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
