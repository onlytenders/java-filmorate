package com.practice.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.filmorate.model.User;
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
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateUserWithEmptyEmail() {
        User user = new User(null, "", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Почта не может быть пустой", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateUserWithInvalidEmail() {
        User user = new User(null, "invalid-email", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Похоже, это не электронная почта", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateUserWithEmptyLogin() {
        User user = new User(null, "test@example.com", "", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Логин не может быть пустой", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateUserWithLoginContainingSpaces() {
        User user = new User(null, "test@example.com", "login with spaces", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Логин не может содержать пробелы", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateUserWithFutureBirthday() {
        User user = new User(null, "test@example.com", "login", "Name", LocalDate.of(2026, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Дата рождения не может быть в будущем", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testCreateUserWithEmptyName() {
        User user = new User(null, "test@example.com", "login", "", LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@example.com", response.getBody().getEmail());
        assertEquals("login", response.getBody().getLogin());
        assertEquals("login", response.getBody().getName());
        assertEquals(LocalDate.of(1990, 1, 1), response.getBody().getBirthday());
    }

    @Test
    void testUpdateUserWithNullId() {
        User user = new User(null, "test@example.com", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(user), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("ID пользователя не указан", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }

    @Test
    void testUpdateUserNotFound() {
        User user = new User(999L, "test@example.com", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(user), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(response.getBody(), Map.class);
            String errorMessage = (String) errorResponse.get("detail");
            assertEquals("Пользователь с ID 999 не найден", errorMessage);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось распарсить JSON-ответ", e);
        }
    }
}