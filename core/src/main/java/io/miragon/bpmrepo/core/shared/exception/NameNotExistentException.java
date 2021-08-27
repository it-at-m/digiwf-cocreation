package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NameNotExistentException extends RuntimeException {
    public NameNotExistentException(final String customText) {
        super(customText);
    }
}
