package ru.practicum.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.*", "ru.practicum"})
public class EwmServerApp {
    public static void main(String[] args) {
        SpringApplication.run(EwmServerApp.class, args);
    }
}
