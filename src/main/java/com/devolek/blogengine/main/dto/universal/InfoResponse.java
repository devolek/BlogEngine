package com.devolek.blogengine.main.dto.universal;

import lombok.Data;

@Data
public class InfoResponse implements Response{
    private String title;
    private String subtitle;
    private String phone;
    private String email;
    private String copyright;
    private String copyrightFrom;

    public InfoResponse() {
        title = "DevPub";
        subtitle = "Рассказы разработчиков";
        phone = "+7 965 787-12-34";
        email = "mail@mail.ru";
        copyright = "Наумов Валентин";
        copyrightFrom = "2020";
    }
}
