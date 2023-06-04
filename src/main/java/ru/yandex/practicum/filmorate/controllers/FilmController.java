package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@Component
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService service;
    private final UserService userService;

    @GetMapping()
    public  List<Film> getFilms() {

        log.info("get all films request");
        return service.getAllFilms();
    }

    @GetMapping("/popular")
    public List<Film> getRatedFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("get rated films request");
        return service.ratedFilms(service.getAllFilms(), count);
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("get film request");
        return service.getFilm(id);
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        validate(film);

        return service.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
            validate(film);

        return service.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        userService.getUser(userId);
        service.addLike(service.getFilm(id), userId);
        log.info("Film " + id + " got like from user " + userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        userService.getUser(userId);
        service.delLike(service.getFilm(id), userId);
        log.info("user " + userId + " removed like from film " + id);
    }

    private void validate(Film film) {
        if (film.getName() == null ||
                film.getName().isEmpty() ||
                film.getDescription().length() > 200 ||
                film.getDuration() < 1 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Validation exception in attempting create a film");
            throw new ValidationException();
        }
    }
}
