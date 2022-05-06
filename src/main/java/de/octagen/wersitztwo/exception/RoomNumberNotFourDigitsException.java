package de.octagen.wersitztwo.exception;

public class RoomNumberNotFourDigitsException extends RuntimeException{
    public RoomNumberNotFourDigitsException(String message) {
        super(message);
    }
}
