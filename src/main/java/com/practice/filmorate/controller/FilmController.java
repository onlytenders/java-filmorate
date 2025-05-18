package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import com.practice.filmorate.storage.FilmStorage;
import com.practice.filmorate.storage.InMemoryFilmStorage;
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

    private final FilmStorage films = new InMemoryFilmStorage();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return films.addFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films.getAllFilms();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return films.updateFilm(film);
    }
}
