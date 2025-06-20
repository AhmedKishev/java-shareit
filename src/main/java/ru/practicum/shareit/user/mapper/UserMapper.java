package ru.practicum.shareit.user.mapper;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@AllArgsConstructor
public class UserMapper {

    public static User toUser(UserDto userDto) {

        return new User(1L,
                userDto.getName(),
                userDto.getEmail());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getName(),
                user.getEmail());
    }
}
