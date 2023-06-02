package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class InMemoryFilmStorage  implements FilmStorage {
    private final HashMap<Integer, Film> storage = new HashMap<>();

    public List<Film> getAllFilms() {

        return new ArrayList<>(storage.values());
    }

    public Film getFilm(int id) {
        if (storage.get(id) == null) {
            throw new NoSuchFilmException("such user doesn't exist");
        }

        return storage.get(id);
    }

    public void addFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        storage.put(film.getId(), film);
    }
}
