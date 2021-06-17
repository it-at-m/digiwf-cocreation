package io.miragon.bpmnrepo.core.user.domain.exception;

import io.miragon.bpmnrepo.core.shared.exception.NameConflictException;

public class UsernameAlreadyInUseException extends NameConflictException {
    public UsernameAlreadyInUseException(String username){
        super(String.format("The username '%s' is already in use", username));
    }
}
