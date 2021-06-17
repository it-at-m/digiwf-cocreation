package io.miragon.bpmnrepo.core.user.domain.business;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(HttpStatus.CONFLICT)
public class NameAlreadyInUseException extends RuntimeException{
    public NameAlreadyInUseException(final String message){
        super(message);
        log.warn(message);
    }
}
