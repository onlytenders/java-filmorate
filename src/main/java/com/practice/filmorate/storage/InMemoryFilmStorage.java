package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();

    @Override
    public Long addFilm(Film film) {
        films.add(film);
        log.info("Добавлен новый фильм: {}", films);
        return film.getId();
}

    @Override
    public Film updateFilm(Film film) {
        log.info("Обновлен фильм: {}", film.getName());
        films.set(film.getId().intValue(), film);
        return film;
    }

    @Override
    public Film deleteFilm(Long id) {
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(id)) {
                return films.remove(i);
            }
        }

        log.info("Удален фильм с ID: {}", id);

        return null;
    }

    @Override
    public Film getFilmById(Long id) {

        for (Film film : films) {
            if (film.getId().equals(id)) {
                return film;
            }
        }

        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }

    @Override
    public Long hitLike(Long filmId, Long userId) {
        getFilmById(filmId).getLiked().add(userId);
        return userId;
    }

    @Override
    public Long removeLike(Long filmId, Long userId) {
        getFilmById(filmId).getLiked().remove(userId);
        return userId;
    }
}
