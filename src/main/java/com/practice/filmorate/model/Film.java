package com.practice.filmorate.model;

import com.practice.filmorate.annotations.NotEarlierThan;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @NotEarlierThan(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть больше 0")
    private int duration;

    @UniqueElements(message = "Этот пользователь уже поставил лайк")
    private Set<Long> liked = new HashSet<>();
}
