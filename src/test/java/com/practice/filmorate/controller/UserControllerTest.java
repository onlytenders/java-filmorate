package com.practice.filmorate.controller;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testCreateUserWithEmptyEmail() {
        User user = new User(null, "", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Почта не может быть пустой", response.getBody());
    }

    @Test
    void testCreateUserWithInvalidEmail() {
        User user = new User(null, "invalid-email", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Похоже, это не электронная почта", response.getBody());
    }

    @Test
    void testCreateUserWithEmptyLogin() {
        User user = new User(null, "test@example.com", "", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Логин не может быть пустой", response.getBody());
    }

    @Test
    void testCreateUserWithLoginContainingSpaces() {
        User user = new User(null, "test@example.com", "login with spaces", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Логин не может содержать пробелы", response.getBody());
    }

    @Test
    void testCreateUserWithFutureBirthday() {
        User user = new User(null, "test@example.com", "login", "Name", LocalDate.of(2026, 1, 1));
        ResponseEntity<String> response = restTemplate.postForEntity("/users", user, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Дата рождения не может быть в будущем", response.getBody());
    }

    @Test
    void testCreateUserWithEmptyName() {
        User user = new User(null, "test@example.com", "login", "", LocalDate.of(1990, 1, 1));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("login", response.getBody().getName());
    }

    @Test
    void testUpdateUserWithNullId() {
        User user = new User(null, "test@example.com", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(user), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("ID пользователя не указан", response.getBody());
    }

    @Test
    void testUpdateUserNotFound() {
        User user = new User(999L, "test@example.com", "login", "Name", LocalDate.of(1990, 1, 1));
        ResponseEntity<String> response = restTemplate.exchange("/users", HttpMethod.PUT, new HttpEntity<>(user), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Пользователь с ID 999 не найден", response.getBody());
    }
}