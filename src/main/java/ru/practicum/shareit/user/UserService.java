package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFound;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    FakeUserRepository userRepository;

    public User save(UserDto user) {
        return userRepository.save(user);
    }

    public User getUserById(long id) {
        Optional<User> findUser = userRepository.getUserById(id);
        if (findUser.isEmpty()) {
            throw new ObjectNotFound("Такого пользователя не существует");
        } else return findUser.get();
    }


    public void remove(long id) {
        getUserById(id);
        userRepository.remove(id);
    }

    public User edit(long id, UserDto user) {
        getUserById(id);
        return userRepository.edit(user, id);
    }
}
