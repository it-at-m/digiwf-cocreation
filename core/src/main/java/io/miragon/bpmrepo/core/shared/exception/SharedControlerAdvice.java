package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class SharedControlerAdvice {

    @ExceptionHandler(NameConflictException.class)
    public ResponseEntity<String> handleOperationNotAllowed(final NameConflictException exception) {
        log.warn("Client error", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

}
