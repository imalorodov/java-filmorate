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
    private int id = 1;

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
    public User addUser(User user) {
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        user.setId(id);
        storage.put(id++, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (storage.get(user.getId()) == null) {
            throw new NoSuchUserException("impossible to update user with id " + user.getId());
        }
        if (user.getFriends() == null) {
            user.setFriends(new HashSet<>());
        }
        storage.put(user.getId(), user);

        return user;
    }

    @Override
    public void deleteUser(int id) {
        if (storage.get(id) == null) {
            throw new NoSuchUserException("impossible to delete user with id " + id);
        }
        storage.remove(id);
    }
}
