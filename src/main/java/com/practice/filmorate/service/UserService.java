package com.practice.filmorate.service;

import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.UserStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public UserService(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public User addFriend(Long userId, Long friendId) {
        User user = userStorage.getUserById(userId);
        return null;
    }

    public User deleteFriend(Long userId, Long friendId) {
        return null;
    }

    public Set<User> getAllFriends(Long id) {
        return null;
    }

    public Set<User> mutualFriends(Long userId, Long friendId) {
        return null;
    }

}
