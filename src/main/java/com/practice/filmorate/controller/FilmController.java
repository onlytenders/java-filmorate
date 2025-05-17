package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(idGen.getAndIncrement());
        films.add(film);
        log.info("Добавлен новый фильм: {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрошены все фильмы");
        return films;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                log.info("Обновлен фильм: {}", films.get(i).getName());
                films.set(i, film);
                return film;
            }
        }

        log.error("Пользователь с ID {} не найден", film.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + film.getId() + " не найден");
    }
}
