package io.miragon.bpmrepo.core.artifact.domain.exception;

public class EditDeployedVersionException extends RuntimeException {
    public EditDeployedVersionException(final String customText) {
        super(customText);
    }
}