package ru.practicum.shareit.item;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.ObjectNotFound;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.comment.model.Comment;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CommentRepository commentRepository;

    private User owner;
    private User booker;
    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Owner");
        owner.setEmail("owner@example.com");
        owner = userRepository.save(owner);

        booker = new User();
        booker.setName("Booker");
        booker.setEmail("booker@example.com");
        booker = userRepository.save(booker);

        item = new Item();
        item.setName("Item");
        item.setDescription("Description");
        item.setAvailable(true);
        item.setOwner(owner);
        item = itemRepository.save(item);

        itemDto = new ItemDto();
        itemDto.setName("New Item");
        itemDto.setDescription("New Description");
        itemDto.setAvailable(true);
    }

    @Test
    void save() {
        Item savedItem = itemService.save(owner.getId(), itemDto);

        assertNotNull(savedItem.getId());
        assertEquals(itemDto.getName(), savedItem.getName());
        assertEquals(itemDto.getDescription(), savedItem.getDescription());
        assertTrue(savedItem.isAvailable());
    }

    @Test
    void edit() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Item");
        updateDto.setDescription("Updated Description");
        updateDto.setAvailable(false);

        Item updatedItem = itemService.edit(item.getId(), updateDto, owner.getId());

        assertEquals(item.getId(), updatedItem.getId());
        assertEquals(updateDto.getName(), updatedItem.getName());
        assertEquals(updateDto.getDescription(), updatedItem.getDescription());
        assertEquals(updateDto.getAvailable(), updatedItem.isAvailable());
    }


    @Test
    void getItemsByUser() {
        List<Item> items = itemService.getItemsByUser(owner.getId());

        assertFalse(items.isEmpty());
        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
    }

    @Test
    void getItemsByText() {

        List<Item> emptyResult = itemService.getItemsByText("");
        assertTrue(emptyResult.isEmpty());


        List<Item> foundItems = itemService.getItemsByText("desc");
        assertFalse(foundItems.isEmpty());
        assertEquals(item.getId(), foundItems.get(0).getId());
    }

    @Test
    void addComment() {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        booking.setStatus(Status.APPROVED);
        bookingRepository.save(booking);

        CommentDto commentDto = new CommentDto();
        commentDto.setText("Test comment");

        CommentDtoOut addedComment = itemService.addComment(commentDto, booker.getId(), item.getId());

        assertNotNull(addedComment.getId());
        assertEquals(commentDto.getText(), addedComment.getText());
        assertEquals(booker.getName(), addedComment.getAuthorName());
    }

    @Test
    void getAllCommentByItemId() {
        Comment comment = new Comment();
        comment.setText("Test comment");
        comment.setItem(item);
        comment.setAuthor(booker);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        List<CommentDtoOut> comments = itemService.getAllCommentByItemId(booker.getId(), item.getId());

        assertFalse(comments.isEmpty());
        assertEquals(1, comments.size());
        assertEquals(comment.getText(), comments.get(0).getText());
    }

    @Test
    void edit_WhenNotOwner_ShouldThrowException() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Item");

        assertThrows(ObjectNotFound.class,
                () -> itemService.edit(item.getId(), updateDto, booker.getId()));
    }
}