package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private UserDto testUserDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUserDto = new UserDto();
        testUserDto.setName("Test User");
        testUserDto.setEmail("test@example.com");

        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser = userRepository.save(testUser);
    }

    @Test
    void saveUserTest() {
        User savedUser = userService.save(testUser);

        assertNotNull(savedUser.getId());
        assertEquals(testUser.getName(), savedUser.getName());
        assertEquals(testUser.getEmail(), savedUser.getEmail());


        User fromDb = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(fromDb);
        assertEquals(savedUser.getName(), fromDb.getName());
    }

    @Test
    void getUserById() {
        User foundUser = userService.getUserById(testUser.getId());

        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
        assertEquals(testUser.getName(), foundUser.getName());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void remove() {
        userService.remove(testUser.getId());

        assertFalse(userRepository.existsById(testUser.getId()));
    }

    @Test
    void edit() {
        UserDto updateDto = new UserDto();
        updateDto.setName("Updated Name");
        updateDto.setEmail("updated@example.com");

        User updatedUser = userService.edit(testUser.getId(), updateDto);

        assertEquals(testUser.getId(), updatedUser.getId());
        assertEquals(updateDto.getName(), updatedUser.getName());
    }
}