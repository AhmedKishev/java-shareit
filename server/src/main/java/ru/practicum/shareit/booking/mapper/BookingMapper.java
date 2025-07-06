package ru.practicum.shareit.booking.mapper;


import lombok.AllArgsConstructor;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDtoSave;
import ru.practicum.shareit.booking.dto.BookingDtoShort;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;

@AllArgsConstructor
public class BookingMapper {
    static ItemRepository itemRepository;

    public static BookingDtoSave toBookingDto(Booking booking) {
        return new BookingDtoSave(booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId());
    }

    public static Booking toBooking(BookingDtoSave bookingDtoSave, Item findItem, User findUser) {
        return new Booking(1L,
                bookingDtoSave.getStart(),
                bookingDtoSave.getEnd(),
                findItem,
                findUser,
                Status.WAITING);
    }

    public static BookingDtoShort toBookingDtoShort(Booking booking) {
        return new BookingDtoShort(booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker().getId());
    }
}
