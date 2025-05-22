package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film addFilm(Film film);
    Film updateFilm(Film film);
    Film deleteFilm(Long id);
    Film getFilmById(Long id);
    List<Film> getAllFilms();

    Long hitLike(Long filmId, Long userId);
    Long removeLike(Long filmId, Long userId);
}