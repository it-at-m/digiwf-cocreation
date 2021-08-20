package io.miragon.bpmrepo.core.shared.exception;


public class ObjectNotFoundException extends NullPointerException {
    public ObjectNotFoundException(final String customText) {
        super(customText);
    }

    public ObjectNotFoundException() {
        super("error.ObjectNotFound");
    }
}
