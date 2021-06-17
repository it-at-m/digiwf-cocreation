package io.miragon.bpmnrepo.core.diagram.domain.exception;

import io.miragon.bpmnrepo.core.shared.exception.NameConflictException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiagramNameAlreadyInUseException extends NameConflictException {
    public DiagramNameAlreadyInUseException(){
        super("Diagram name duplicated - please choose another name");
    }
}
