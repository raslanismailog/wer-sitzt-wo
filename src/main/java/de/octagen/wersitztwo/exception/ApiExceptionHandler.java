package de.octagen.wersitztwo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApiExceptionHandler extends DefaultHandlerExceptionResolver {


    @ExceptionHandler(value = {DuplicateRoomException.class})
    public ResponseEntity<Object> handleDuplicateRoomException(DuplicateRoomException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(2, e.getMessage());
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {DuplicateResidentException.class})
    public ResponseEntity<Object> handleDuplicateResidentException(DuplicateResidentException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(3, e.getMessage());
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {CsvLineNotValidException.class})
    public ResponseEntity<Object> handleCsvLineNotValidException(CsvLineNotValidException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(4, e.getMessage());
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {RoomNotFountException.class})
    public ResponseEntity<Object> handleRoomNotFountException(RoomNotFountException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(5, e.getMessage());
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = {RoomNumberNotFourDigitsException.class})
    public ResponseEntity<Object> handleRoomNumberNotFourDigitsException(RoomNumberNotFourDigitsException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(6, e.getMessage());
        return new ResponseEntity<>(apiException, status);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;
        ApiException apiException = new ApiException(7,"Request method "+ e.getMethod() +" not allowed" );
        return new ResponseEntity<>(apiException, status);
    }
}
