package de.muenchen.oss.digiwf.cocreation.core.shared.exception;

public class NameConflictException extends RuntimeException {
    public NameConflictException(final String customText) {
        super(customText);
    }
}
