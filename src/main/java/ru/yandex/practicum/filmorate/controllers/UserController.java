package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping()
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@RequestBody User user){
        validate(user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        logger.info("User '" + user.getName() + "' added");

        return users.get(id - 1);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if (users.get(user.getId()) == null) {
            logger.warn("Attempting to update not existing user!");
            throw new NoSuchElementException();
        }
        users.put(user.getId(), user);
        logger.info("User '" + user.getName() + "' with id: " + user.getId() + " updated.");
        return users.get(user.getId());
    }

    private void validate(User user) {
        if (user.getEmail().isEmpty() || !user.getEmail().contains("@") || user.getLogin() == null ||
                !user.getLogin().trim().equals(user.getLogin()) || user.getBirthday().isAfter(LocalDate.now())) {
            logger.warn("Validation exception in attempting create an user ");
            throw new ValidationException();
        }
    }
}
