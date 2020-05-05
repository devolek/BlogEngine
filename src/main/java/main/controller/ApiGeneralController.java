package main.controller;

import main.model.BlogInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    @GetMapping("/api/init/")
    public ResponseEntity getInit(){
        return new ResponseEntity(new BlogInformation("DevPub", "Рассказы разработчиков",
                "+7 965 787-12-34", "mail@mail.ru", "Наумов Валентин",
                "2020"), HttpStatus.OK);
    }

}
