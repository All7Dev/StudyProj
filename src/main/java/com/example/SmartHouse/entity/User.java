package com.example.SmartHouse.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity                                 // Этот класс будет таблицей в БД
@Table(name = "users")                  // Имя таблицы
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class User {

    @Id                                 // Первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоинкремент
    private Long id;

    @Column(unique = true, nullable = false) // Уникальное, не может быть пустым
    private String username;            // Логин пользователя

    @Column(nullable = false)
    private String password;            // Пароль (будет храниться в зашифрованном виде)

    private boolean enabled = true;     // Активен ли пользователь (если false — не может войти)

    // Связь многие-ко-многим: у пользователя может быть много ролей, одна роль может быть у многих пользователей
    @ManyToMany(fetch = FetchType.EAGER) // EAGER означает, что при загрузке пользователя сразу загрузятся и его роли
    @JoinTable(                         // Таблица-связка между users и roles
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>(); // Список ролей пользователя
}