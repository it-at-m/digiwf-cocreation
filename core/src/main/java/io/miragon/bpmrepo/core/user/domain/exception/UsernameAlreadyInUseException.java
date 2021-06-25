package io.miragon.bpmrepo.core.user.domain.exception;

import io.miragon.bpmrepo.core.shared.exception.NameConflictException;

public class UsernameAlreadyInUseException extends NameConflictException {
    public UsernameAlreadyInUseException(final String username) {
        super(String.format("The username '%s' is already in use", username));
    }
}
