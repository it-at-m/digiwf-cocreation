package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class AccessRightException extends RuntimeException {
    public AccessRightException(final String customText) {
        super(customText);
    }
}
