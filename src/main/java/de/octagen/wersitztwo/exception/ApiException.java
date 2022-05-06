package de.octagen.wersitztwo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter @AllArgsConstructor
public class ApiException {
    private final int code;
    private final String message;
}
