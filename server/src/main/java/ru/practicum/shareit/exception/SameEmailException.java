package ru.practicum.shareit.exception;

public class SameEmailException extends RuntimeException {
    public SameEmailException(String e) {
        super(e);
    }
}
