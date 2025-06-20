package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item save(Long userId, ItemDto item);

    Item edit(long itemId, ItemDto editItem, Long userId);

    ItemDtoOut getItemById(long itemId);

    List<Item> getItemsByUser(long userId);

    List<Item> getItemsByText(String text);

    CommentDtoOut addComment(CommentDto commentDto, long userId, long itemId);

    List<CommentDtoOut> getAllCommentByItemId(long userId, long itemId);

}
