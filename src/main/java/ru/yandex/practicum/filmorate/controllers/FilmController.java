package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
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
    private static final Logger logger = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> movies = new HashMap<>();
    private int id = 1;

    @GetMapping()
    public List<Film> getFilms() { 
        return new ArrayList<>(movies.values());
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film){
        validate(film);
        film.setId(id++);
        movies.put(film.getId(), film);
        logger.info("Film '" + film.getName() + "' added");
        return movies.get(id - 1);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film){
        if(movies.get(film.getId()) == null){
            logger.warn("Attempting to update not existing film!");
            throw new NoSuchElementException();
        } else {
            movies.put(film.getId(), film);
            logger.info("Film '" + film.getName() + "' with id: " + film.getId() + " updated.");
        }
        return movies.get(film.getId());
    }

    private void validate(Film film) {
        if(film.getName().isEmpty() || film.getDescription().length() > 200 || film.getDuration() < 1 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            logger.warn("Validation exception in attempting create a film");
            throw new ValidationException();
        }
    }
}
