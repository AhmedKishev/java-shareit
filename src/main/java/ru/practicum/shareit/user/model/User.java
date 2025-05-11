package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class User {
    Long id;
    @NotBlank
    String name;
    @Email
    @NotBlank
    String email;
}
