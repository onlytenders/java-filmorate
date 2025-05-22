package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    User deleteUser(Long id);
    User getUserById(Long id);
    List<User> getAllUsers();

    User addFriend(Long idUser, Long idFriend);
    User removeFriend(Long idUser, Long idFriend);
    Set<Long> getAllFriends(Long idUser);

    Set<Long> getAllLikes(Long idUser);

    Long hitLike(Long userId, Long filmId);
    Long removeLike(Long userId, Long filmId);
}