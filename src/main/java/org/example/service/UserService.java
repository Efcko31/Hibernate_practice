package org.example.service;

import org.example.entity.BasketItem;
import org.example.entity.Product;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserService {
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
            System.out.println("товар добавлен в корзину!");
        } catch (Exception e) {
            System.out.println("Не добавлен товар: " + e.getMessage());
        }
    }
}
