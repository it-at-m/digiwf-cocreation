package io.miragon.bpmnrepo.core.shared.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserNotExistentException extends RuntimeException{

    public UserNotExistentException(){
        super("InitialAccess - create new user!");
        log.error("InitialAccess - create new user!");
    }
}

