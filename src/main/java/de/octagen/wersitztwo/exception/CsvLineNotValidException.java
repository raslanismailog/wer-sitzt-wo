package de.octagen.wersitztwo.exception;

public class CsvLineNotValidException extends RuntimeException{
    public CsvLineNotValidException(String message) {
        super(message);
    }
}
