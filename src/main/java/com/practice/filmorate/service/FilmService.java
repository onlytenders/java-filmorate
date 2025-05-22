package com.practice.filmorate.service;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.UserStorage;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final AtomicLong idGen = new AtomicLong(1);

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Long addFilm(@Valid Film film) {
        film.setId(idGen.getAndIncrement());
        return filmStorage.addFilm(film);
    }

    public Long updateFilm(@Valid Film film) {
        if (film.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        if (filmStorage.getFilmById(film.getId()) != null) {
            filmStorage.updateFilm(film);
        }

        log.error("Фильм с ID {} не найден", film.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + film.getId() + " не найден");
    }

    public Film deleteFilm(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        if (filmStorage.getFilmById(id) != null) {
            return filmStorage.deleteFilm(id);
        }

        log.error("Фильм с ID {} не найден", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
    }

    public Film getFilmById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        Film film = filmStorage.getFilmById(id);

        if (film != null) {
            return film;
        } else {
            log.error("Фильм с ID {} не найден", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Long hitLike(Long filmId, Long userId) {

        if (filmId == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма или пользователя не указан");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            log.error("Фильм с ID {} не найден", filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + filmId + " не найден");
        }

        if (userStorage.getUserById(userId) == null) {
            log.error("Пользовательы с ID {} не найден", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + filmId + " не найден");
        }

        userStorage.hitLike(userId, filmId);
        return filmStorage.hitLike(filmId, userId);
    }

    public Long removeLike(Long filmId, Long userId) {

        if (filmId == null || userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма или пользователя не указан");
        }

        if (filmStorage.getFilmById(filmId) == null) {
            log.error("Фильм с ID {} не найден", filmId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + filmId + " не найден");
        }

        if (userStorage.getUserById(userId) == null) {
            log.error("Пользовательы с ID {} не найден", userId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь с ID " + filmId + " не найден");
        }

        userStorage.removeLike(userId, filmId);
        return filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getTopFilms(int count) {

        if (count <= 0) {
            log.error("Некорректная величина топа");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректная величина топа");
        }

        return getAllFilms().stream()
                .sorted( (f1, f2) -> Integer.compare( f2.getLiked().size(), f1.getLiked().size() ) )
                .limit(count)
                .collect(Collectors.toList());
    }
}
