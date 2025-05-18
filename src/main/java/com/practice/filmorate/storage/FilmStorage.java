package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film deleteFilm(Long id);
    Film getFilmById(Long id);
    List<Film> getAllFilms();
}