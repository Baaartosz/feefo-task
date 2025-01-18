package dev.baaart.feefo.task.exception;

public class NoMatchException extends RuntimeException {
    public NoMatchException(String message) {
        super(message);
    }
}