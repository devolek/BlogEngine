package com.devolek.blogengine.main.exeption;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException(String message, String error) {
        super(error, message);
    }

    public UserNotFoundException(String message) {
        super("invalid request", message);
    }

    public UserNotFoundException(Integer id) {
        super("invalid request", String.format("Person with id = %d not found", id));
    }
}
