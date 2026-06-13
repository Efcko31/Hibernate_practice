package org.example.service;

import org.example.entity.BasketItem;
import org.example.entity.Product;
import org.example.entity.User;
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

            User user = session.get(User.class, userId);
            Product product = session.get(Product.class, productId);

            if(user != null && product != null) {
                BasketItem basketItem = new BasketItem();
                basketItem.setUser(user);
                basketItem.setProduct(product);
                basketItem.setQuantity(quantity);
                user.addBasketItem(basketItem);

                session.persist(basketItem);
            }
            transaction.commit();
            System.out.println("товар добавлен в корзину");
        } catch (Exception e) {
            System.out.println("Не добавлен товар в корзину, причина: " + e.getMessage());
        }
    }

    public void create(String username, String pass) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(pass);
        userRepository.save(newUser);
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
                                item.getProduct().getName(), item.getQuantity())
                );
            }
        }, () -> System.out.println("Пользователь не найден!"));
    }

    public void updateUser(User updateData) {
        userRepository.findById(updateData.getId()).ifPresentOrElse(user -> {
            System.out.println(String.format("Приветствую %s !", user.getUsername()));

            userRepository.update(updateData);
        }, () -> System.out.println("Пользователь не найден!"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
