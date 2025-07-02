package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDtoSave;

import java.util.List;

public interface BookingService {
    Booking addBooking(BookingDtoSave booking, Long userId);

    Booking approveBooking(Long userId, long bookingId, boolean approved);

    Booking getBookingById(Long userId, long id);

    List<Booking> getAllBookingByBooker(long id, String state);

    List<Booking> getAllBookingByOwner(Long userId, String state);
}
