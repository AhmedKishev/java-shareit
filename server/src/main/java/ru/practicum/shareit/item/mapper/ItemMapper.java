package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        return new Item(0L,
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable());
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getName(),
                item.getDescription(),
                item.isAvailable());
    }

    public static ItemDtoOut toItemDtoOut(Item item, List<CommentDtoOut> comments, BookingDtoShort last, BookingDtoShort next) {
        if (last == null) {
            return new ItemDtoOut(item.getId(),
                    item.getOwner(), item.getName(),
                    item.getDescription(),
                    item.isAvailable(),
                    comments,
                    null,
                    null);
        }
        return new ItemDtoOut(item.getId(),
                item.getOwner(), item.getName(),
                item.getDescription(),
                item.isAvailable(),
                comments,
                last,
                next);
    }
}
