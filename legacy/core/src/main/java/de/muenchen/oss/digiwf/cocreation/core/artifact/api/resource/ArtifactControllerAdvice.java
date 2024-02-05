package de.muenchen.oss.digiwf.cocreation.core.artifact.api.resource;

import de.muenchen.oss.digiwf.cocreation.core.shared.exception.NameConflictException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ArtifactControllerAdvice {

    @ExceptionHandler({NameConflictException.class})
    public ResponseEntity<String> handleNameConflictException(final NameConflictException exception) {
        log.warn("Client error: ", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

}
