package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccessRightException extends RuntimeException {
    public AccessRightException(final String additionalText) {
        super("Access denied: " + additionalText);
        log.error("Access denied: " + additionalText);
    }

    public AccessRightException() {
        super("You don't have the permission to perform this action.");
        log.error("You don't have the permission to perform this action.");
    }
}
