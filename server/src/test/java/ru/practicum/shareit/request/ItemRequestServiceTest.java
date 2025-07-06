package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    private User requester;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        requester = new User();
        requester.setName("Requester");
        requester.setEmail("requester@example.com");
        requester = userRepository.save(requester);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Need item for testing");
    }

    @Test
    void save() {
        ItemRequest savedRequest = itemRequestService.save(itemRequestDto, requester.getId());

        assertNotNull(savedRequest.getId());
        assertEquals(itemRequestDto.getDescription(), savedRequest.getDescription());
        assertEquals(requester.getId(), savedRequest.getRequester().getId());
    }

    @Test
    void getItemRequestById() {
        ItemRequest savedRequest = itemRequestService.save(itemRequestDto, requester.getId());
        ItemRequestDto foundRequest = itemRequestService.getItemRequestById(savedRequest.getId(), requester.getId());

        assertEquals(savedRequest.getId(), foundRequest.getId());
        assertEquals(savedRequest.getDescription(), foundRequest.getDescription());
    }

    @Test
    void getAllItemRequests() {
        itemRequestService.save(itemRequestDto, requester.getId());
        List<ItemRequestDto> requests = itemRequestService.getAllItemRequests(requester.getId());

        assertFalse(requests.isEmpty());
        assertEquals(1, requests.size());
        assertEquals(itemRequestDto.getDescription(), requests.get(0).getDescription());
    }

}