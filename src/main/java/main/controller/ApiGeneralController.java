package main.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ApiGeneralController {

    @GetMapping("/api/init/")
    public Map<String, Object> getInit(){
        HashMap<String, Object> model = new HashMap<>();
        model.put("title", "DevPub");
        model.put("subtitle", "Рассказы разработчиков");
        model.put("phone", "+7 965 787-12-34");
        model.put("email", "mail@mail.ru");
        model.put("copyright", "Наумов Валентин");
        model.put("copyrightFrom", "2020");
        return model;
    }

}
