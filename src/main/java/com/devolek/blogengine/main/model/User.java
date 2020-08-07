package com.devolek.blogengine.main.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id пользователя

    @Column(name = "is_moderator", nullable = false, columnDefinition = "TINYINT")
    private int isModerator; //является ли пользователь модератором (может ли править глобальные настройки сайта и модерировать посты)

    @Column(name = "reg_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Calendar regTime; //дата и время регистрации пользователя

    @Column(nullable = false)
    private String name; //имя пользователя

    @Column(nullable = false)
    private String email; //e-mail пользователя

    @Column(nullable = false)
    private String password; //хэш пароля пользователя

    @Column
    private String code; //код для восстановления пароля, может быть NULL

    @Column(columnDefinition = "TEXT")
    private String photo; //фотография (ссылка на файл), может быть NULL

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Post> posts;
}
