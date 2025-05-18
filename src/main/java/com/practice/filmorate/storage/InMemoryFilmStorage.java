package com.practice.filmorate.storage;

import com.practice.filmorate.model.Film;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public Film addFilm(Film film) {
        film.setId(idGen.getAndIncrement());
        films.add(film);
        log.info("Добавлен новый фильм: {}", films);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                log.info("Обновлен фильм: {}", films.get(i).getName());
                films.set(i, film);
                return films.get(i);
            }
        }

        log.error("Фильм с ID {} не найден", film.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + film.getId() + " не найден");
    }

    @Override
    public Film deleteFilm(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(id)) {
                return films.remove(i);
            }
        }

        log.error("Фильм с ID {} не найден", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
    }

    @Override
    public Film getFilmById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID фильма не указан");
        }

        for (Film film : films) {
            if (film.getId().equals(id)) {
                return film;
            }
        }

        log.error("Фильм с ID {} не найден", id);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с ID " + id + " не найден");
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }
}
