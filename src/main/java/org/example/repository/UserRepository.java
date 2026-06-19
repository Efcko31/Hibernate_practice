package org.example.repository;

import org.example.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void save(UserEntity userEntity);

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByIdWithBasket(Long id);

    List<UserEntity> findAll();

    void update(UserEntity userEntity);

    void deleteById(Long id);
}
