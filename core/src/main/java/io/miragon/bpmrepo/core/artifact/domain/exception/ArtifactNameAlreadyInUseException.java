package io.miragon.bpmrepo.core.artifact.domain.exception;

import io.miragon.bpmrepo.core.shared.exception.NameConflictException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArtifactNameAlreadyInUseException extends NameConflictException {
    public ArtifactNameAlreadyInUseException() {
        super("Artifact name duplicated - please choose another name");
    }
}
