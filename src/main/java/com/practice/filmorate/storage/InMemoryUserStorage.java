package com.practice.filmorate.storage;

import com.practice.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();

    @Override
    public Long addUser(User user) {
        users.add(user);
        log.info("Добавлен новый пользователь: " + user.getLogin());
        return user.getId();
    }

    @Override
    public User updateUser(User user) {
        log.info("Обновлен пользователь: {}", user.getLogin());
        users.set(user.getId().intValue(), user);
        return user;
    }

    @Override
    public User deleteUser(Long id) {

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                return users.remove(i);
            }
        }

        log.info("Удален пользователь с ID: {}", id);

        return null;

    }

    @Override
    public User getUserById(Long id) {

        log.info("Запрошен пользователь с ID: {}", id);

        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрошены все пользователи");
        return users;
    }

    @Override
    public User addFriend(Long idUser, Long idFriend) {
        getUserById(idUser).getFriends().add(idFriend);
        getUserById(idFriend).getFriends().add(idUser);
        log.info("Пользователь {} добавил в друзья {}", idUser, idFriend);
        return getUserById(idUser);
    }

    @Override
    public User removeFriend(Long idUser, Long idFriend) {
        getUserById(idUser).getFriends().remove(idFriend);
        getUserById(idFriend).getFriends().remove(idUser);
        log.info("Пользователь {} убрал из друзей {}", idUser, idFriend);
        return getUserById(idUser);
    }

    @Override
    public Set<Long> getAllFriends(Long idUser) {
        log.info("Список друзей пользователя {}", idUser);
        return getUserById(idUser).getFriends();
    }

    @Override
    public Set<Long> getAllLikes(Long idUser) {
        log.info("Запрошены все лайки пользователя {}", idUser);
        return getUserById(idUser).getLikedFilms();
    }

    @Override
    public Long hitLike(Long userId, Long filmId) {
        getUserById(userId).getLikedFilms().add(filmId);
        return filmId;
    }

    @Override
    public Long removeLike(Long userId, Long filmId) {
        getUserById(userId).getLikedFilms().remove(filmId);
        return filmId;
    }
}
