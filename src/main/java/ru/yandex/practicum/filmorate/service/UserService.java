package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void delFriend(User user, User userToDel) {
        user.getFriends().remove(userToDel.getId());
        userToDel.getFriends().remove(user.getId());
    }

    public List<User> friendList(int id) {

        return storage.getUser(id).getFriends().stream().map(this::getUser).collect(Collectors.toList());
    }

    public List<User> commonFriends(int userId, int friendId) {
        List<Integer> f = storage.getUser(userId).getFriends().stream().
                filter(storage.getUser(friendId).getFriends()::contains).
                collect(Collectors.toList());

        return f.stream().map(this::getUser).collect(Collectors.toList());
    }

    public User getUser(int id) {
       return storage.getUser(id);
    }

    public List<User> getUsers() {
        return storage.getAllUsers();
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User update(User user) {
        return storage.updateUser(user);
    }

    public void deleteUser(int id) {
        storage.deleteUser(id);
    }
}
