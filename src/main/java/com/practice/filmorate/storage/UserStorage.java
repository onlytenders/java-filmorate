package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    User deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();
}