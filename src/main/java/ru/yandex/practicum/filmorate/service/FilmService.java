package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotPositiveCountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage storage;

    public void addLike(Film film, int id) {
        film.getLikes().add(id);
    }

    public void delLike(Film film, int id) {
        film.getLikes().remove(id);
    }

    public List<Film> ratedFilms(List<Film> allFilms, int count) {
        if (count <= 0) {
            throw new NotPositiveCountException();
        }
        if (allFilms.size() <= count) {
            return allFilms;
        } else {
            allFilms.sort((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()));
            return allFilms.stream().limit(count).collect(Collectors.toList());
        }
    }

    public List<Film> getAllFilms() {
        return storage.getAllFilms();
    }

    public Film getFilm(int id) {
        return storage.getFilm(id);
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return storage.updateFilm(film);
    }
}
