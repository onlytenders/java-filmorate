package com.practice.filmorate.service;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.UserStorage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final AtomicLong idGen = new AtomicLong(1);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(@Valid User user) {
        user.setId(idGen.getAndIncrement());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        userStorage.addUser(user);
        return user;
    }

    public User updateUser(@Valid User user) {
        if (user.getId() == null) {
            log.error("ID пользователя не указан");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        if (userStorage.getUserById(user.getId()) != null) {
            return userStorage.updateUser(user);
        }

        log.error("Пользователь с ID {} не найден", user.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + user.getId() + " не найден");
    }

    public User getUserById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        User user = userStorage.getUserById(id);

        if (user != null) {
            return user;
        } else {
            log.error("Пользователь с ID {} не найден", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + id + " не найден");
        }
    }

    public User deleteUser(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        if (userStorage.getUserById(id) != null) {
            return userStorage.deleteUser(id);
        }

        log.error("Пользователь с ID {} не найден", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + id + " не найден");
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addFriend(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) != null && userStorage.getUserById(friendId) != null) {
            return userStorage.addFriend(userId, friendId);
        }

        log.error("Пользователь или друг не найдены");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь или друг не найдены");
    }

    public User removeFriend(Long userId, Long friendId) {
        if (userStorage.getUserById(userId) != null && userStorage.getUserById(friendId) != null) {
            return userStorage.removeFriend(userId, friendId);
        }

        log.error("Пользователь или друг не найдены");
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь или друг не найдены");
    }

    public Set<Long> getAllFriends(Long idUser) {
        if (userStorage.getUserById(idUser) != null) {
            return userStorage.getAllFriends(idUser);
        }

        log.error("Пользователь {} не найден", idUser);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь " + idUser + " не найден");
    }

    public Set<Long> getAllLikes(Long idUser) {
        if (userStorage.getUserById(idUser) != null) {
            return userStorage.getAllLikes(idUser);
        }

        log.error("Пользователь {} не найден", idUser);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь " + idUser + " не найден");
    }

    public Set<Long> mutualFriends(Long userId, Long friendId) {

        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь или друг не найдены");
        }

        Set<Long> intersection = new HashSet<>(userStorage.getAllFriends(userId));
        intersection.retainAll(userStorage.getAllFriends(friendId));
        return intersection;
    }

    public Set<Long> mutualLikes(Long userId, Long friendId) {

        if (userStorage.getUserById(userId) == null || userStorage.getUserById(friendId) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь или друг не найдены");
        }

        Set<Long> intersection = new HashSet<>(userStorage.getAllLikes(userId));
        intersection.retainAll(userStorage.getAllLikes(friendId));
        return intersection;
    }
}
