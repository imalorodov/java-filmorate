package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;

public class FilmValidationTest {
    FilmController controller;
    Film film = new Film();

    @BeforeEach
    void init() {
        controller = new FilmController();
    }

    @Test
    void positiveValidation() {
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1993, 1, 24));
        film.setDuration(100);

        controller.addFilm(film);

        Assertions.assertEquals("[Film(id=1, name=name, description=Description," +
                " releaseDate=1993-01-24, duration=100)]", controller.getFilms().toString());
    }

    @Test
    void negativeValidationByEmptyName() {
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1993, 1, 24));
        film.setDuration(100);

        Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(film));
    }

    @Test
    void negativeValidationByLongDescription() {
        film.setName("name");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                " а именно 20 миллионов. о Куглов, который за время «своего отсутствия»," +
                " стал кандидатом Коломбани.");
        film.setReleaseDate(LocalDate.of(1993, 1, 24));
        film.setDuration(100);

        Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(film));
    }

    @Test
    void negativeValidationByRealiseDate() {
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100);

        Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(film));
    }

    @Test
    void negativeValidationByNotPositiveDuration() {
        film.setName("name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1993, 1, 24));
        film.setDuration(0);

        Assertions.assertThrows(ValidationException.class,
                () -> controller.addFilm(film));
    }
}
