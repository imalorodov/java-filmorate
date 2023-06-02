package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    public void delFriend(User user, User userToDel) {
        user.getFriends().remove(userToDel.getId());
        userToDel.getFriends().remove(user.getId());
    }

    public List<Integer> commonFriends(User user, User friend) {
        List<Integer> toReturn = new ArrayList<>();
        for(Integer i : user.getFriends()) {
            if (friend.getFriends().contains(i)) {
                toReturn.add(i);
            }
        }
        return toReturn;
    }
}
