package com.example.SmartHouse.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity                                 // Этот класс будет таблицей в БД
@Table(name = "users")                  // Имя таблицы
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
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
// ручное внесение геттеров и сеттеров для устранения ошибки компиляции
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}