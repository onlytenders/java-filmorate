package com.practice.filmorate.controller;

import com.practice.filmorate.model.Film;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateFilmWithEmptyName() {
        Film film = new Film(null, "", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Название фильма не может быть пустым", response.getBody());
    }

    @Test
    void testCreateFilmWithTooLongDescription() {
        String longDescription = "a".repeat(201); // 201 символ
        Film film = new Film(null, "Test Film", longDescription, LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Описание не должно превышать 200 символов", response.getBody());
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(1895, 12, 27), 120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", response.getBody());
    }

    @Test
    void testCreateFilmWithNegativeDuration() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(2000, 1, 1), -120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Продолжительность фильма должна быть положительной", response.getBody());
    }

    @Test
    void testUpdateFilmWithNullId() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, new HttpEntity<>(film), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID фильма не указан", response.getBody());
    }

    @Test
    void testUpdateFilmNotFound() {
        Film film = new Film(999L, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, new HttpEntity<>(film), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Фильм с ID 999 не найден", response.getBody());
    }

    @Test
    void testCreateFilmWithValidData() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Film", response.getBody().getName());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCreateFilmWithBoundaryDescription() {
        String boundaryDescription = "a".repeat(200); // Ровно 200 символов
        Film film = new Film(null, "Test Film", boundaryDescription, LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(boundaryDescription, response.getBody().getDescription());
    }
}