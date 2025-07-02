package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    static final String pathThrowId = "/{id}";
    UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping(pathThrowId)
    public User getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping(pathThrowId)
    public void deleteUserById(@PathVariable long id) {
        userService.remove(id);
    }

    @PatchMapping(pathThrowId)
    public User editUser(@RequestBody UserDto user,
                         @PathVariable Long id) {
        return userService.edit(id, user);
    }
}
