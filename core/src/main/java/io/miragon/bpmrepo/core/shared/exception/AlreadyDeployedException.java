package io.miragon.bpmrepo.core.shared.exception;

public class AlreadyDeployedException extends RuntimeException {
    public AlreadyDeployedException(final String customText) {
        super(customText);
    }
}
