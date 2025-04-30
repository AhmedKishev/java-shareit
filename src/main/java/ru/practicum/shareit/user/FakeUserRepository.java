package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.SameEmailException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FakeUserRepository {
    Map<Long, User> FAKE_USERS = new HashMap<>();

    private void findWithSameEmail(UserDto user) {
        Optional<User> findUser = FAKE_USERS.values().stream().filter(user1 -> user.getEmail().equals(user1.getEmail())).findFirst();
        if (findUser.isPresent()) {
            throw new SameEmailException("Такой email уже занят");
        }
    }

    public User save(UserDto user) {
        findWithSameEmail(user);
        User saveUser = UserMapper.toUser(user);
        saveUser.setId(getId());
        FAKE_USERS.put(saveUser.getId(), saveUser);
        return saveUser;
    }

    private long getId() {
        return FAKE_USERS.size() + 1;
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(FAKE_USERS.get(id));
    }

    public void remove(long id) {
        FAKE_USERS.remove(id);
    }

    public User edit(UserDto user, long id) {
        User findUser = FAKE_USERS.get(id);
        if (user.getEmail() != null) {
            findWithSameEmail(user);
        }
        if (user.getName() != null) {
            findUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            findUser.setEmail(user.getEmail());
        }
        return findUser;
    }
}
