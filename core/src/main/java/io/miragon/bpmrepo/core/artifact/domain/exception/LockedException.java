package io.miragon.bpmrepo.core.artifact.domain.exception;

public class LockedException extends RuntimeException {
    public LockedException(final String customText) {
        super(customText);
    }
}
