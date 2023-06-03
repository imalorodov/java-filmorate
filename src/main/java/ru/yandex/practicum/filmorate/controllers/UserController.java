package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@Component
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    @GetMapping()
    public List<User> getUsers() {

        log.info("Get all users request");
        return service.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {

        log.info("get user " + id + " request");
        return service.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> friendList(@PathVariable int id) {

        log.info("Friends list request");
        return service.friendList(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable int id, @PathVariable int otherId) {

        log.info("Common friends list request");
        return service.commonFriends(id, otherId);
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {
        validate(user);
        checkName(user);
        log.info("User '" + user.getName() + "' added");

        return service.addUser(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        validate(user);
        checkName(user);
        user = service.update(user);
        log.info("User '" + user.getName() + "' with id: " + user.getId() + " updated.");

        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId)  {
        service.addFriend(service.getUser(id), service.getUser(friendId));
        log.info("Users " + id + " and " + friendId + " became friends");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable int id, @PathVariable int friendId) {
        service.delFriend(service.getUser(id), service.getUser(friendId));
        log.info("User " + id + " deleted " + friendId + " from friends list");
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
        log.info("user" + id + "deleted");
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
