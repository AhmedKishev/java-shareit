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
    Map<Long, User> fakeUsers = new HashMap<>();

    private void findWithSameEmail(UserDto user) {
        Optional<User> findUser = fakeUsers.values().stream().filter(user1 -> user.getEmail().equals(user1.getEmail())).findFirst();
        if (findUser.isPresent()) {
            throw new SameEmailException("Такой email уже занят");
        }
    }

    public User save(UserDto user) {
        findWithSameEmail(user);
        User saveUser = UserMapper.toUser(user);
        saveUser.setId(getId());
        fakeUsers.put(saveUser.getId(), saveUser);
        return saveUser;
    }

    private long getId() {
        return fakeUsers.size() + 1;
    }

    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(fakeUsers.get(id));
    }

    public void remove(long id) {
        fakeUsers.remove(id);
    }

    public User edit(UserDto user, long id) {
        User findUser = fakeUsers.get(id);
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
