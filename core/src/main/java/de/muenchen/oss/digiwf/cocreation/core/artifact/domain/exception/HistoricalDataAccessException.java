package de.muenchen.oss.digiwf.cocreation.core.artifact.domain.exception;

public class HistoricalDataAccessException extends RuntimeException {
    public HistoricalDataAccessException(final String customText) {
        super(customText);
    }
}
