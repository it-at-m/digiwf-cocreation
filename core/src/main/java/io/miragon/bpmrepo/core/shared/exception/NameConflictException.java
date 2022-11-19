package io.miragon.bpmrepo.core.shared.exception;

public class NameConflictException extends RuntimeException {
    public NameConflictException(final String customText) {
        super(customText);
    }
}
