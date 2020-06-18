package com.devolek.blogengine.main.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "global_settings")
public class GlobalSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //id настройки

    @Column(nullable = false)
    private String code; //системное имя настройки

    @Column(nullable = false)
    private String name; //название настройки

    @Column(nullable = false)
    private String value; //значение настройки

}
