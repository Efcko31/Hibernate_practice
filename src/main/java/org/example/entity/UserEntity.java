package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_age")
    private Integer age;
    @Column(name = "create_at")
    private LocalDateTime createAt;
    @Column(name = "hash_for_hash")
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BasketItemEntity> productBasket;

    public void addBasketItem(BasketItemEntity item) {
        if(this.productBasket == null) {
            this.productBasket = new ArrayList<>();
        }
        this.productBasket.add(item);
        item.setUserEntity(this);
    }
}
