package org.example;

import org.example.entity.UserEntity;
import org.example.service.UserService;
import org.example.util.HibernateUtil;

import java.util.Scanner;

public class MainTestClass {
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
                case 5 -> updateUserData();
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

        System.out.println("Введите username");
        String username = scanner.nextLine();

        System.out.println("Придумайте пароль");
        String pass = scanner.nextLine();

        userService.create(username, pass);
    }

    private static void showAllUsers() {
        System.out.println("\n--- Список пользователей в БД ---");
        userService.showAllUsers();
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
        userService.showUserBasket(userId);

    }

    private static void updateUserData() {
        UserEntity updateData = new UserEntity();

        System.out.print("Введите ID пользователя: ");
        updateData.setId(scanner.nextLong());
        scanner.nextLine(); //очищаем буфер

        System.out.print("Введите новое имя пользователя ");
        updateData.setUsername(scanner.nextLine());

        System.out.print("Введите новую почту пользователя ");
        updateData.setEmail(scanner.nextLine())
        ;
        System.out.print("Введите новый возраст пользователя ");
        updateData.setAge(scanner.nextInt());
        scanner.nextLine();

        userService.updateUser(updateData);
    }

    private static void deleteUser() {
        System.out.print("Введите ID пользователя для удаления: ");
        Long id = scanner.nextLong();

        userService.deleteUser(id);
    }
}
