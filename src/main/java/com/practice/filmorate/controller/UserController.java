package com.practice.filmorate.controller;

import com.practice.filmorate.model.User;
import com.practice.filmorate.storage.InMemoryUserStorage;
import com.practice.filmorate.storage.UserStorage;
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

    private final UserStorage users = new InMemoryUserStorage();

    @GetMapping
    public List<User> getAllUsers() {
        return users.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return users.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return users.updateUser(user);
    }
}
