package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoSave;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookingController {

    BookingService bookingService;
    String sharerUserId = "X-Sharer-User-Id";

    @PostMapping
    public Booking addBooking(@RequestHeader(sharerUserId) Long userId,
                              @RequestBody BookingDtoSave booking) {
        return bookingService.addBooking(booking, userId);
    }

    @PatchMapping("/{id}")
    public Booking approveBooking(@RequestHeader(sharerUserId) Long userId,
                                  @PathVariable long id,
                                  @RequestParam boolean approved) {
        return bookingService.approveBooking(userId, id, approved);
    }

    @GetMapping("/{id}")
    public Booking getBookingById(@RequestHeader(sharerUserId) Long userId,
                                  @PathVariable long id) {
        return bookingService.getBookingById(userId, id);
    }

    @GetMapping
    public List<Booking> getAllBookingByBooker(@RequestHeader(sharerUserId) Long userId,
                                               @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByBooker(userId, state);
    }


    @GetMapping("/owner")
    public List<Booking> getAllBookingByOwner(@RequestHeader(sharerUserId) Long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") String state) {

        return bookingService.getAllBookingByOwner(userId, state);
    }
}
