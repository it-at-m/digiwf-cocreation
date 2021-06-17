package io.miragon.bpmnrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.CONFLICT)
public class NameConflictException extends RuntimeException{
    public NameConflictException(String additionalText){
        super("Conflict: " + additionalText);
        log.error("Conflict: " + additionalText);
    }

    public NameConflictException(){
        super("Conflict: value has to be unique");
        log.error("Conflict: value has to be unique");
    }
}
