package ru.practicum.shareit.exception;

public class FailBooking extends RuntimeException {
    public FailBooking(String message) {
        super(message);
    }
}
