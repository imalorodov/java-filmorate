package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage  implements FilmStorage {
    private final HashMap<Integer, Film> storage = new HashMap<>();
    private int id = 1;

    public List<Film> getAllFilms() {

        return new ArrayList<>(storage.values());
    }

    public Film getFilm(int id) {
        if (storage.get(id) == null) {
            throw new NoSuchFilmException("film with id " + id + " doesn't exist");
        }

        return storage.get(id);
    }

    public Film addFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        film.setId(id);
        storage.put(id++, film);
        log.info("Film '" + film.getName() + " added");
        return film;
    }

    public Film updateFilm(Film film) {
        if (storage.get(film.getId()) == null) {
            throw new NoSuchFilmException("film with id " + film.getId() + " doesn't exist");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        storage.put(film.getId(), film);
        log.info("Film '" + film.getName() + "' with id: " + film.getId() + " updated.");
        return film;
    }
}
