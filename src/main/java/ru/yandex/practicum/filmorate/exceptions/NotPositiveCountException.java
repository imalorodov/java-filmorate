package ru.yandex.practicum.filmorate.exceptions;

public class NotPositiveCountException extends RuntimeException {

    @Override
    public String getMessage() {
        return "variable count have to be more then 0!";
    }
}
