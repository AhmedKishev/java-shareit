package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Booking {
    LocalDateTime start;
    LocalDateTime end;
    Item reservation;
    User booker;
    Status status;
}
