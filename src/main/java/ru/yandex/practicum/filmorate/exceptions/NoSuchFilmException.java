package ru.yandex.practicum.filmorate.exceptions;

public class NoSuchFilmException extends RuntimeException {

    public NoSuchFilmException(String s) {
        super(s);
    }
}
