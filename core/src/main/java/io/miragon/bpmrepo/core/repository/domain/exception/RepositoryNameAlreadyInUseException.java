package io.miragon.bpmrepo.core.repository.domain.exception;

import io.miragon.bpmrepo.core.shared.exception.NameConflictException;

public class RepositoryNameAlreadyInUseException extends NameConflictException {
    public RepositoryNameAlreadyInUseException() {
        super("Repository name duplicated - please choose another name");
    }
}
