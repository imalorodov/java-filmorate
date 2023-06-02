package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoSuchUserException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> storage = new HashMap<>();

    @Override
    public List<User> getAllUsers() {

        return new ArrayList<>(storage.values());
    }

    @Override
    public User getUser(int id) {
        if (storage.get(id) == null) {
            throw new NoSuchUserException("such user doesn't exist");
        }

        return storage.get(id);
    }

    @Override
    public void addUser(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        storage.put(user.getId(), user);
    }
}
