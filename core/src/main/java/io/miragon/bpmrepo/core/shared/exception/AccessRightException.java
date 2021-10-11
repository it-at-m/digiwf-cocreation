package io.miragon.bpmrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessRightException extends RuntimeException {
    public AccessRightException(final String customText) {
        super(customText);
    }
}
