package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoSave;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.FailBooking;
import ru.practicum.shareit.exception.NameException;
import ru.practicum.shareit.exception.ObjectNotFound;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.states.State;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public Booking addBooking(BookingDtoSave booking, Long userId) {
        checkTime(booking);
        checkIdItem(booking.getItemId());
        Item findItem = itemRepository.findById(booking.getItemId()).get();
        User findUser = userRepository.findById(userId).get();
        if (!findItem.isAvailable()) {
            throw new FailBooking("Вещь недоступна для брони");
        }
        return bookingRepository.save(BookingMapper.toBooking(booking, findItem, findUser));
    }

    @Override
    public Booking approveBooking(Long userId, long bookingId, boolean approved) {
        Booking bookingById = bookingRepository.findById(bookingId).get();
        if (!bookingById.getItem().getOwner().getId().equals(userId)) {
            throw new FailBooking("Данный пользователь не является владельцем вещи");
        }
        if (approved) bookingById.setStatus(Status.APPROVED);
        else bookingById.setStatus(Status.REJECTED);
        return bookingById;
    }

    @Override
    public Booking getBookingById(Long userId, long id) {
        Booking bookingById = bookingRepository.findById(id).get();
        if (!bookingById.getItem().getOwner().getId().equals(userId) && !bookingById.getBooker().getId().equals(userId)) {
            throw new FailBooking("Данный пользователь не является ни арендодателем, ни заказчиком");
        }
        return bookingById;
    }

    @Override
    public List<Booking> getAllBookingByBooker(long id, String state) {
        User booker = userRepository.findById(id).get();
        State bookingState = State.valueOf(state);
        List<Booking> bookings = List.of();
        switch (bookingState) {
            case ALL -> bookings = bookingRepository.findAllByBookerId(booker.getId(), Sort.by(DESC, "start"));
            case PAST ->
                    bookings = bookingRepository.findAllByBookerIdAndStatePast(booker.getId(), Sort.by(DESC, "start"));
            case CURRENT ->
                    bookings = bookingRepository.findAllByBookerIdAndStateCurrent(booker.getId(), Sort.by(DESC, "start"));
            case FUTURE ->
                    bookings = bookingRepository.findAllByBookerIdAndStateFuture(booker.getId(), Sort.by(DESC, "start"));
            case REJECTED -> bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                    State.REJECTED, Sort.by(DESC, "end"));
            case WAITING -> bookings = bookingRepository.findAllByBookerIdAndStatus(booker.getId(),
                    State.WAITING, Sort.by(DESC, "start"));

        }
        return bookings;
    }

    @Override
    public List<Booking> getAllBookingByOwner(Long userId, String state) {
        User owner = userRepository.findById(userId).get();
        if (owner == null) {
            throw new FailBooking("Такого владельца вещей не существует");
        }
        State bookingState = State.valueOf(state);
        List<Booking> bookings = List.of();
        switch (bookingState) {
            case ALL -> bookings = bookingRepository.findAllByOwnerId(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case CURRENT -> bookings = bookingRepository.findAllByOwnerIdAndStateCurrent(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case PAST -> bookings = bookingRepository.findAllByOwnerIdAndStatePast(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case FUTURE -> bookings = bookingRepository.findAllByOwnerIdAndStateFuture(owner.getId(),
                    Sort.by(Sort.Direction.DESC, "start"));
            case WAITING -> bookings = bookingRepository.findAllByOwnerIdAndStatus(owner.getId(),
                    State.WAITING, Sort.by(Sort.Direction.DESC, "start"));
            case REJECTED -> bookings = bookingRepository.findAllByOwnerIdAndStatus(owner.getId(),
                    State.REJECTED, Sort.by(Sort.Direction.DESC, "start"));
        }
        return bookings;
    }


    private void checkIdItem(Long itemId) {
        if (itemId > itemRepository.count()) throw new ObjectNotFound("Предмета с таким id нету: " + itemId);
    }


    private void checkTime(BookingDtoSave bookingDtoSave) {
        if (bookingDtoSave.getStart() == null) {
            throw new FailBooking("Ошибка со временем начала");
        }
        if (bookingDtoSave.getEnd() == null) {
            throw new FailBooking("Ошибка со временем конца");
        }
        if (!bookingDtoSave.getEnd().isAfter(bookingDtoSave.getStart()) ||
                bookingDtoSave.getStart().isBefore(LocalDateTime.now()))
            throw new NameException("Неправильно составлено время");
    }


}
