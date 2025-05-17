package com.practice.filmorate.model;

import com.practice.filmorate.annotations.NotEarlierThan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Название не должно првышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не может быть пустой")
    @NotEarlierThan(message = "Дата релиза не может быть раньше 28 декабря 1985 года")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private int duration;
}
