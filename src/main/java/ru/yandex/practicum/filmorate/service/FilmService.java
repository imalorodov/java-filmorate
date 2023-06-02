package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public void addLike(Film film, int id) {
        film.getLikes().add(id);
    }

    public void delLike(Film film, int id) {
        film.getLikes().remove(id);
    }

    public List<Film> ratedFilms(List<Film> allFilms, int count) {
        if (allFilms.size() <= count) {
            return allFilms;
        } else {
            allFilms.sort((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()));
            return allFilms.stream().limit(count).collect(Collectors.toList());
        }
    }
}
