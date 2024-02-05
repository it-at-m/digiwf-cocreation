package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.exception;

public class LockedException extends RuntimeException {
    public LockedException(final String customText) {
        super(customText);
    }
}
