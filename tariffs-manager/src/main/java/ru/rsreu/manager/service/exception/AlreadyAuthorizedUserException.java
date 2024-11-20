package ru.rsreu.manager.service.exception;

public class AlreadyAuthorizedUserException extends RuntimeException {
    public AlreadyAuthorizedUserException(String message) {
        super(message);
    }
}
