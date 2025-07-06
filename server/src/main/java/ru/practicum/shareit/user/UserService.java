package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;


public interface UserService {
    User save(User user);

    User getUserById(Long userId);

    void remove(Long userId);

    User edit(Long userId, UserDto userEdit);
}
