package org.example.repository;

import org.example.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByIdWithBasket(Long id);

    List<User> findAll();

    void update(User user);

    void deleteById(Long id);
}
