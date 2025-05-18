package com.practice.filmorate.model;

import com.practice.filmorate.validators.validationGroups.NotBlankGroup;
import com.practice.filmorate.validators.validationGroups.PatternGroup;
import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({NotBlankGroup.class, PatternGroup.class, User.class})
public class User {
    private Long id;

    @NotBlank(message = "Почта не может быть пустой", groups = NotBlankGroup.class)
    @Email(message = "Похоже, это не электронная почта")
    private String email;

    @NotBlank(message = "Логин не может быть пустой", groups = NotBlankGroup.class)
    @Pattern(regexp = "\\S+", message = "Логин не может содержать пробелы", groups = PatternGroup.class)
    private String login;

    private String name;

    @NotNull(message = "Дата рождения не может быть пустой")
    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
