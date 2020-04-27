package main.model;

import javax.persistence.*;
import java.util.Calendar;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsModerator() {
        return isModerator;
    }

    public void setIsModerator(int isModerator) {
        this.isModerator = isModerator;
    }

    public Calendar getRegTime() {
        return regTime;
    }

    public void setRegTime(Calendar regTime) {
        this.regTime = regTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
