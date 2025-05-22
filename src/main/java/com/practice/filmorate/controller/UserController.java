package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.model.User;
import com.practice.filmorate.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public Long addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}/friends")
    public Set<Long> getAllFriends(@PathVariable Long id) {
        return userService.getAllFriends(id);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/likes")
    public Set<Long> getAllLikes(@PathVariable Long id) {
        return userService.getAllLikes(id);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Set<Long> mutualFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.mutualFriends(userId, friendId);
    }

    @GetMapping("/{userId}/likes/{friendId}/mutual")
    public Set<Long> mutualLikes(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.mutualLikes(userId, friendId);
    }
}
