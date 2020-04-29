package main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Calendar;

@Data
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

}
