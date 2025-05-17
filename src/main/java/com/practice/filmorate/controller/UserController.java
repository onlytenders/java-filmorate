package com.practice.filmorate.controller;

import com.practice.filmorate.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрошены все пользователи");
        return users;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {

        user.setId(idGen.getAndIncrement());

        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }

        users.add(user);

        log.info("Добавлен новый пользователь: " + user.getLogin());

        return user;

    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if (user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID пользователя не указан");
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                log.info("Обновлен пользователь: {}", users.get(i).getLogin());
                users.set(i, user);
                return user;
            }
        }

        log.error("Пользователь с ID {} не найден", user.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + user.getId() + " не найден");
    }
}
