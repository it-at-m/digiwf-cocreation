package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class SharedControlerAdvice {

    @ExceptionHandler({NameConflictException.class})
    public ResponseEntity<String> handleOperationNotAllowed(final NameConflictException exception) {
        log.error("Client error", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("repository.nameTaken");
    }

    @ExceptionHandler({AccessRightException.class})
    public ResponseEntity<String> handleAccessRight(final AccessRightException exception) {
        log.error("Client error", exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body("EXC_NAME_CONFLICT");
    }

}
