package org.example;

import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;
import org.example.entity.User;
import org.example.service.UserService;
import org.example.util.HibernateUtil;

import java.util.Scanner;

public class MainTestClass {
    private static final UserRepository userRepository = new UserRepositoryImpl();
    private static final UserService userService = new UserService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==== КОНСОЛЬНЫЙ УПРАВЛЕНЕЦ HIBERNATE ====");

        while (true) {
            System.out.println("\n      ГЛАВНОЕ МЕНЮ ");
            System.out.println("1. Создать нового пользователя");
            System.out.println("2. Показать всех пользователей");
            System.out.println("3. Добавить товар в корзину пользователя");
            System.out.println("4. Посмотреть корзину пользователя (Защита от Lazy)");
            System.out.println("5. Изменить имя пользователя");
            System.out.println("6. Удалить пользователя (вместе с корзиной)");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createNewUser();
                case 2 -> showAllUsers();
                case 3 -> addProductToBasket();
                case 4 -> showUserBasket();
                case 5 -> updateUsername();
                case 6 -> deleteUser();
                case 0 -> {
                    System.out.println("Закрытие сессий и выход...");
                    HibernateUtil.shutdown();
                    return;
                }
                default -> System.out.println("Неверный пункт меню!");
            }
        }
    }

    private static void createNewUser() {
        User user = new User();
        System.out.print("Введите username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Введите пароль: ");
        user.setPassword(scanner.nextLine());

        userRepository.save(user);
    }

    private static void showAllUsers() {
        System.out.println("\n--- Список пользователей в БД ---");
        userRepository.findAll().forEach(user ->
                System.out.printf("ID: %d | Логин: %s \n",
                        user.getId(), user.getUsername())
        );
    }

    private static void addProductToBasket() {
        System.out.print("Введите ID пользователя: ");
        Long userId = scanner.nextLong();
        System.out.print("Введите ID продукта из каталога (например, 1 или 2): ");
        Long productId = scanner.nextLong();
        System.out.print("Количество: ");
        Integer quantity = scanner.nextInt();

        userService.addProductToUserBasket(userId, productId, quantity);
    }

    private static void showUserBasket() {
        System.out.print("Введите ID пользователя для просмотра корзины: ");
        Long userId = scanner.nextLong();

       userRepository.findByIdWithBasket(userId).ifPresentOrElse(user -> {
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

    private static void updateUsername() {
        System.out.print("Введите ID пользователя: ");
        Long id = scanner.nextLong();
        scanner.nextLine();

        userRepository.findById(id).ifPresentOrElse(user -> {
            System.out.print("Введите новый username (текущий: " + user.getUsername() + "): ");
            user.setUsername(scanner.nextLine());

            userRepository.update(user);
        }, () -> System.out.println("Пользователь не найден!"));
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = scanner.nextLong();

        userRepository.deleteById(id);
    }
}
