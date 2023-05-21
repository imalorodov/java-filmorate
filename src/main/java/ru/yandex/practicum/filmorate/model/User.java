package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
}