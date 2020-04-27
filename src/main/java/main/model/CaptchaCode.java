package main.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id; //id каптча

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time; //дата и время генерации кода капчи

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private int code; //код, отображаемый на картинкке капчи

    @Column(name = "secret_code", nullable = false, columnDefinition = "TINYTEXT")
    private int secretCode; //код, передаваемый в параметре

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getSecretCode() {
        return secretCode;
    }

    public void setSecretCode(int secretCode) {
        this.secretCode = secretCode;
    }
}
