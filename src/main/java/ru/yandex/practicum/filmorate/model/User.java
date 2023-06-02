package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Integer> friends;
}
