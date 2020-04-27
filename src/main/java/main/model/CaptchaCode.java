package main.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Calendar time;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private int code;

    @Column(name = "secret_code", nullable = false, columnDefinition = "TINYTEXT")
    private int secretCode;

}
