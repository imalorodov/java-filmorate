package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.exceptions.NotPositiveCountException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Component
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage storage;
    private final FilmService service;
    private int id = 1;

    @GetMapping()
    public  List<Film> getFilms() {

        log.info("get request");
        return new ArrayList<>(storage.getAllFilms());
    }

    @GetMapping("/popular")
    public List<Film> getRatedFilms(@RequestParam(defaultValue = "10") String count) {
        if (Integer.parseInt(count) <= 0) {
            throw new NotPositiveCountException();
        }
        return service.ratedFilms(storage.getAllFilms(), Integer.parseInt(count));
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable String id) {
        int movieId = Integer.parseInt(id);

        if (movieId < 1) {
            throw new NoSuchFilmException("id have to be positive");
        }
        Film film;
        try {
            film = storage.getFilm(Integer.parseInt(id));
        } catch (NoSuchFilmException e) {
            throw new NoSuchFilmException("such film doesn't exist");
        }
        return film;
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        validate(film);
        film.setId(id++);
        storage.addFilm(film);
        log.info("Film '" + film.getName() + "' added");
        return storage.getFilm(film.getId());
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        if (storage.getFilm(film.getId()) == null) {
            log.warn("Attempting to update not existing film!");
            throw new NoSuchFilmException("such film doesn't exist");
        } else {
            validate(film);
            storage.addFilm(film);
            log.info("Film '" + film.getName() + "' with id: " + film.getId() + " updated.");
        }
        return storage.getFilm(film.getId());
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable String id, @PathVariable String userId) {
        int idUser = Integer.parseInt(userId);
        int movieId = Integer.parseInt(id);
        Film film;

        try {
            film = storage.getFilm(movieId);
        } catch (NoSuchFilmException e) {
            throw new NoSuchFilmException(e.getMessage());
        }
        service.addLike(film, idUser);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable String id, @PathVariable String userId) {
        int movieId = Integer.parseInt(id);
        int idUser = Integer.parseInt(userId);

        if (movieId < 1 || idUser < 1) {
            throw new NoSuchFilmException("id have to be positive");
        }
        Film film;
        try {
            film = storage.getFilm(movieId);
        } catch (NullPointerException e) {
            throw new NoSuchFilmException("such film doesn't exist");
        }
        service.delLike(film, idUser);
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isEmpty() || film.getDescription().length() > 200 || film.getDuration() < 1 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Validation exception in attempting create a film");
            throw new ValidationException();
        }
    }

    @ExceptionHandler
    public Map<String, String> numberFormatHandler(final NumberFormatException e) {
        return Map.of("error", "given wrong format of number in the path",
                "errorMessage", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> memoryFilmHandler(final NoSuchFilmException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> countHandler(final NotPositiveCountException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public  Map<String, String> validateHandler(final ValidationException e) {
        return Map.of("error", "validate error such film can not be created");
    }
}
