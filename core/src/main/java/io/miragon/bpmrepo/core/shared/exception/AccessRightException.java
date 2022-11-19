package io.miragon.bpmrepo.core.shared.exception;

public class AccessRightException extends RuntimeException {
    public AccessRightException(final String customText) {
        super(customText);
    }
}
