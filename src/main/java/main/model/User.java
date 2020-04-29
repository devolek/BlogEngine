package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id пользователя

    @Column(name = "is_moderator", nullable = false, columnDefinition = "TINYINT")
    private int isModerator; //является ли пользователь модератором (может ли править глобальные настройки сайта и модерировать посты)

    @Column(name = "reg_time", nullable = false)
    @Temporal(TemporalType.DATE)
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

}
