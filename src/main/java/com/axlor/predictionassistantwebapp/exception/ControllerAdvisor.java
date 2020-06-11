package com.axlor.predictionassistantwebapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

//exception handing help here: https://dzone.com/articles/spring-rest-service-exception-handling-1

//Could try to have one method that can handle any exception...
//They all just build a JSON body based on timestamp and exception message. However, they do 'require' different status codes...

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SnapshotNotFoundException.class)
    public ResponseEntity<Object> handleSnapshotNotFoundException(SnapshotNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SnapshotCountMismatchException.class)
    public ResponseEntity<Object> handleSnapshotCountMismatchException(SnapshotCountMismatchException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSnapshotsInDatabaseException.class)
    public ResponseEntity<Object> handleNoSnapshotsInDatabaseException(NoSnapshotsInDatabaseException ex, WebRequest request){
        return new ResponseEntity<>(getBody(ex), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MarketNotFoundException.class)
    public ResponseEntity<Object> handleMarketNotFoundException(MarketNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getBody(ex), HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> getBody(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return body;
    }
}
