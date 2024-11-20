package ru.rsreu.manager.service.exception;

public class EntityHasOrphansException extends RuntimeException {
    public EntityHasOrphansException(String message) {
        super(message);
    }
}
