package io.miragon.bpmrepo.core.artifact.domain.exception;

public class EditDeployedMilestoneException extends RuntimeException {
    public EditDeployedMilestoneException(final String customText) {
        super(customText);
    }
}