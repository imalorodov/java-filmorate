package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping()
    public List<User> getUsers() {

        log.info("Get request");
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {
        validate(user);
        checkName(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("User '" + user.getName() + "' added");

        return users.get(user.getId());
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if (users.get(user.getId()) == null) {
            log.warn("Attempting to update not existing user!");
            throw new NoSuchElementException(); /* вытягивается NoSuchElementException потому что он возвращает 500 код,
            что есть в требованиях тестов, а так же подходит по логике,
            клиент пытается обновить объект которого не существует,
            а аповерка валидации происходит ниже, таким образом есть разница между этими двумя ошибками. */
        }
        validate(user);
        checkName(user);
        users.put(user.getId(), user);
        log.info("User '" + user.getName() + "' with id: " + user.getId() + " updated.");
        return users.get(user.getId());
    }

    private void validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getLogin() == null ||
                !user.getLogin().trim().equals(user.getLogin()) || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Validation exception in attempting create an user ");
            throw new ValidationException();
        }
    }

    private void checkName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
