package com.item.exception;

public class ItemAlreadyBookedException extends RuntimeException {
    public ItemAlreadyBookedException(String message) {
        super(message);
    }
}