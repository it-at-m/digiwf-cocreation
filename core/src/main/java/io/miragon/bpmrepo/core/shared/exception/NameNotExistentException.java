package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NameNotExistentException extends RuntimeException {

    public NameNotExistentException() {
        super("This name does not exist.");
        log.error("This name does not exist.");
    }
}
