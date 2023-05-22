package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> movies = new HashMap<>();
    private int id = 1;

    @GetMapping()
    public List<Film> getFilms() {
        log.info("get request");
        return new ArrayList<>(movies.values());
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        validate(film);
        film.setId(id++);
        movies.put(film.getId(), film);
        log.info("Film '" + film.getName() + "' added");
        return movies.get(film.getId());
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        if (movies.get(film.getId()) == null) {
            log.warn("Attempting to update not existing film!");
            throw new NoSuchElementException(); /* вытягивается NoSuchElementException потому что он возвращает 500 код,
            что есть в требованиях тестов, а так же подходит по логике,
            клиент пытается обновить объект которого не существует,
            а аповерка валидации происходит ниже, таким образом есть разница между этими двумя ошибками. */
        } else {
            validate(film);
            movies.put(film.getId(), film);
            log.info("Film '" + film.getName() + "' with id: " + film.getId() + " updated.");
        }
        return movies.get(film.getId());
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getDescription().length() > 200 || film.getDuration() < 1 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Validation exception in attempting create a film");
            throw new ValidationException();
        }
    }
}
