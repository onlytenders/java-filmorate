package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public User addUser(User user) {
        user.setId(idGen.getAndIncrement());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.add(user);

        log.info("Добавлен новый пользователь: " + user.getLogin());

        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                log.info("Обновлен пользователь: {}", users.get(i).getLogin());
                users.set(i, user);
                return users.get(i);
            }
        }

        log.error("Пользователь с ID {} не найден", user.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + user.getId() + " не найден");
    }

    @Override
    public User deleteUser(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                return users.remove(i);
            }
        }

        log.error("Пользователь с ID {} не найден", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + id + " не найден");
    }

    @Override
    public User getUserById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        log.error("Пользователь с ID {} не найден", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + id + " не найден");
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрошены все пользователи");
        return users;
    }
}
