package com.practice.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateFilmWithEmptyName() {
        Film film = new Film(null, "", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Название фильма не может быть пустым", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateFilmWithTooLongDescription() {
        String longDescription = "a".repeat(201);
        Film film = new Film(null, "Test Film", longDescription, LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Название не должно првышать 200 символов", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateFilmWithInvalidReleaseDate() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(1895, 12, 27), 120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateFilmWithNegativeDuration() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(2000, 1, 1), -120);
        ResponseEntity<String> response = restTemplate.postForEntity("/films", film, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Продолжительность фильма должна быть больше 0", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testUpdateFilmWithNullId() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, new HttpEntity<>(film), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("ID фильма не указан", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testUpdateFilmNotFound() {
        Film film = new Film(999L, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<String> response = restTemplate.exchange("/films", HttpMethod.PUT, new HttpEntity<>(film), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Фильм с ID 999 не найден", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateFilmWithValidData() {
        Film film = new Film(null, "Test Film", "Description", LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Film", response.getBody().getName());
        assertEquals("Description", response.getBody().getDescription());
        assertEquals(LocalDate.of(2000, 1, 1), response.getBody().getReleaseDate());
        assertEquals(120, response.getBody().getDuration());
    }

    @Test
    void testCreateFilmWithBoundaryDescription() {
        String boundaryDescription = "a".repeat(200);
        Film film = new Film(null, "Test Film", boundaryDescription, LocalDate.of(2000, 1, 1), 120);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(boundaryDescription, response.getBody().getDescription());
    }
}