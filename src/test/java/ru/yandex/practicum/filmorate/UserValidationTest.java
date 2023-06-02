package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.time.LocalDate;

class UserValidationTest {
    UserController controller;

    @BeforeEach
    void init() {
        controller = new UserController(new InMemoryUserStorage(), new UserService());
    }

    @Test
    void positiveValidation() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1993, 1, 24));

        User userWithoutName = new User();
        userWithoutName.setEmail("email2@mail.ru");
        userWithoutName.setLogin("log");
        userWithoutName.setBirthday(LocalDate.of(1992, 12, 5));

        controller.addUser(user);
        controller.addUser(userWithoutName);

        Assertions.assertEquals("[User(id=1, name=name, email=email@mail.ru," +
                        " login=login, birthday=1993-01-24, friends=null), User(id=2," +
                        " name=log, email=email2@mail.ru, login=log, birthday=1992-12-05, friends=null)]",
                controller.getUsers().toString());
    }

    @Test
    void negativeValidationByWrongEmail() {
        User user = new User();
        user.setName("name");
        user.setEmail("emailMail.ru");
        user.setLogin("login");
        user.setBirthday(LocalDate.of(1993, 1, 24));

        User userWithoutEmail = new User();
        userWithoutEmail.setLogin("log");
        userWithoutEmail.setName("name");
        userWithoutEmail.setBirthday(LocalDate.of(1992, 12, 5));

        Assertions.assertThrows(ValidationException.class,
                () -> {
                    controller.addUser(user);
                    controller.addUser(userWithoutEmail);
                });
    }

    @Test
    void negativeValidationByWrongLogin() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail.ru");
        user.setLogin("login login");
        user.setBirthday(LocalDate.of(1993, 1, 24));

        User userWithoutLogin = new User();
        userWithoutLogin.setName("name");
        userWithoutLogin.setEmail("email@mail.ru");
        userWithoutLogin.setBirthday(LocalDate.of(1993, 1, 24));

        Assertions.assertThrows(ValidationException.class,
                () -> {
                    controller.addUser(user);
                    controller.addUser(userWithoutLogin);
                });
    }

    @Test
    void negativeValidationByTimeInFuture() {
        User user = new User();
        user.setName("name");
        user.setEmail("email@mail.ru");
        user.setLogin("login login");
        user.setBirthday(LocalDate.of(2024, 1, 24));

        Assertions.assertThrows(ValidationException.class,
                () -> controller.addUser(user));
    }
}
