package com.devolek.blogengine.main.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Calendar;

@Data
@Entity
@Table(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id каптча

    @Column(nullable = false)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar time; //дата и время генерации кода капчи

    @Column(nullable = false)
    private String code; //код, отображаемый на картинкке капчи

    @Column(name = "secret_code")
    private String secretCode; //код, передаваемый в параметре

}
