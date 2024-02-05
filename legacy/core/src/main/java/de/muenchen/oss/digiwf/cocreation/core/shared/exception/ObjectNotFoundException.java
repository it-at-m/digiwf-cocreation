package de.muenchen.oss.digiwf.cocreation.core.shared.exception;

public class ObjectNotFoundException extends NullPointerException {
    public ObjectNotFoundException(final String customText) {
        super(customText);
    }
}
