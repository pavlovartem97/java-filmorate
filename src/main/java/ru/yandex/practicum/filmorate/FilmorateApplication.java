package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.controller.GenreController;

@SpringBootApplication
public class FilmorateApplication {

    public static void main(String[] args) {
        var ctx = SpringApplication.run(FilmorateApplication.class, args);
        System.out.println(ctx.getBean(GenreController.class));
    }

}
