package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@Component
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserStorage storage;
    private final UserService service;
    private int id = 1;

    @GetMapping()
    public List<User> getUsers() {

        log.info("Get request");
        return new ArrayList<>(storage.getAllUsers());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        User user;

        if (userId <= 0) {
            log.warn("Attempting of operating with negative id");
            throw new NoSuchUserException("id have to be positive");
        }
        try {
            user = storage.getUser(userId);
        } catch (NoSuchUserException e) {
            log.warn("Attempting of operating with not existing user");
            throw new NoSuchUserException(e.getMessage());
        }
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> friendList(@PathVariable String id) {
        int userId = Integer.parseInt(id);
        User user;

        if (userId < 1) {
            throw new NoSuchUserException("id have to be positive");
        }
        try {
            user  = storage.getUser(userId);
        } catch (NullPointerException e) {
            throw new NoSuchUserException("the user doesn't exist");
        }
        List<User> friends = new ArrayList<>();
        for(Integer i : user.getFriends()){
            friends.add(storage.getUser(i));
        }

        log.info("Friends list request");
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable String id, @PathVariable String otherId) {
        int userId = Integer.parseInt(id);
        int friendId = Integer.parseInt(otherId);

        if (userId < 1 || friendId < 1) {
            log.warn("Attempting of operating with negative id");
            throw new NoSuchUserException("id have to be positive");
        }
        List<User> commonFriends = new ArrayList<>();
        try {
            List<Integer> friendsId = service.commonFriends(storage.getUser(Integer.parseInt(id)),
                    storage.getUser(Integer.parseInt(otherId)));
            for (Integer i : friendsId) {
                commonFriends.add(storage.getUser(i));
            }
        } catch (NullPointerException e) {
            log.warn("Attempting of operating with not existing user");
            throw new NoSuchUserException("one of the users or both are not exist");
        }

        log.info("Common friends list request");
        return commonFriends;
    }

    @PostMapping()
    public User addUser(@RequestBody User user) {
        validate(user);
        checkName(user);
        user.setId(id++);
        storage.addUser(user);

        log.info("User '" + user.getName() + "' added");
        return storage.getUser(user.getId());
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        if (storage.getUser(user.getId()) == null) {
            log.warn("Attempting to update not existing user!");
            throw new NoSuchUserException("not existing user can not be updated");
        }
        validate(user);
        checkName(user);
        storage.addUser(user);

        log.info("User '" + user.getName() + "' with id: " + user.getId() + " updated.");
        return storage.getUser(user.getId());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Map<String, String> pathId)  {
        int userId = Integer.parseInt(pathId.get("id"));
        int friendId = Integer.parseInt(pathId.get("friendId"));
        User user;
        User friend;

        if (userId < 1 || friendId < 1) {
            log.warn("Attempting of operating with negative id");
            throw new NoSuchUserException("id have to be positive");
        }
        try {
            user = storage.getUser(userId);
            friend = storage.getUser(friendId);
        } catch (NullPointerException e) {
            log.warn("Attempting to update not existing user!");
            throw  new NoSuchUserException("one of the users or both are not exist");
        }

        service.addFriend(user, friend);
        log.info("Users " + pathId.get("id") + " and " + pathId.get("friendId") + " became friends");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable String id, @PathVariable String friendId) {
        int userId = Integer.parseInt(id);
        int idFriend = Integer.parseInt(friendId);
        User user;
        User friend;

        if (userId < 1 || idFriend < 1) {
            log.warn("Attempting of operating with negative id");
            throw new NoSuchUserException("id have to be positive");
        }
        try {
            user = storage.getUser(userId);
            friend = storage.getUser(idFriend);
        } catch (NoSuchUserException e) {
            log.warn("Attempting to update not existing user!");
            throw new NoSuchUserException("one of the users or both are not exist");
        }

        service.delFriend(user, friend);
        log.info("User " + id + " deleted " + friendId + " from friends list");
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

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public Map<String, String> numberFormatHandler(final NumberFormatException e) {
        return Map.of("error", "given wrong format of number in the path",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> userHandler(final NoSuchUserException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public  Map<String, String> validateHandler(final ValidationException e) {
        return Map.of("error", "validate error such user can not be created");
    }
}
