package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {
    UserRepository userRepository;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public void remove(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User edit(Long userId, UserDto userEdit) {
        User user = UserMapper.toUser(userEdit);
        User findUser = getUserById(userId);
        updateUser(findUser, userEdit);
        user.setId(userId);
        userRepository.save(findUser);
        log.info(user.toString());
        return user;
    }

    private void updateUser(User updateUser, UserDto user) {
        if (user.getEmail() != null) {
            updateUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            updateUser.setName(user.getName());
        }
    }
}
