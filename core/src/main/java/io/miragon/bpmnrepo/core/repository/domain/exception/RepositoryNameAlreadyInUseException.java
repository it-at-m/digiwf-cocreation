package io.miragon.bpmnrepo.core.repository.domain.exception;

import io.miragon.bpmnrepo.core.shared.exception.NameConflictException;

public class RepositoryNameAlreadyInUseException extends NameConflictException {
    public RepositoryNameAlreadyInUseException(){
        super("Repository name duplicated - please choose another name");
    }
}
