package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.comment.dto.CommentDtoOut;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDtoOut {
    Long id;


    User owner;

    String name;

    String description;

    boolean available;

    List<CommentDtoOut> comments;

    BookingDtoShort lastBooking;

    BookingDtoShort nextBooking;
}
