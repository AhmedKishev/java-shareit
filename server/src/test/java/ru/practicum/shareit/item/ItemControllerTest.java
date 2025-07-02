package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mvc;

    ItemDto itemDto = new ItemDto("aaa", "bbb", true);
    Item item = new Item(1L, new User(), "aaa", "bbb", true);

    @Test
    void addItem() throws Exception {
        when(itemService.save(anyLong(), any(ItemDto.class))).thenReturn(item);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(item.getName())))
                .andExpect(jsonPath("$.description", is(item.getDescription())))
                .andExpect(jsonPath("$.available", is(item.isAvailable())));
    }


    @Test
    void getItemById() throws Exception {
        ItemDtoOut itemDtoOut = new ItemDtoOut(1L, new User(), "aaa", "bbb", true, List.of(), null, null);

        when(itemService.getItemById(anyLong())).thenReturn(itemDtoOut);

        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDtoOut.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoOut.getDescription())));
    }

    @Test
    void getItemsByUser() throws Exception {
        when(itemService.getItemsByUser(anyLong())).thenReturn(List.of(item));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())));

    }

    @Test
    void getItemsByText() throws Exception {
        when(itemService.getItemsByText(anyString())).thenReturn(List.of(item));

        mvc.perform(get("/items/search")
                        .param("text", "drill")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(item.getName())))
                .andExpect(jsonPath("$[0].description", is(item.getDescription())));

    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto("aaa");
        CommentDtoOut commentDtoOut = new CommentDtoOut(1L, "aaa", "bbb", LocalDateTime.now());

        when(itemService.addComment(any(CommentDto.class), anyLong(), anyLong())).thenReturn(commentDtoOut);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1)
                        .content(mapper.writeValueAsString(commentDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text", is(commentDtoOut.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDtoOut.getAuthorName())));
    }
}