package io.payworks.labs.tcpmocker.support.yml;

public class YamlMappingReaderException extends RuntimeException {

    public YamlMappingReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public YamlMappingReaderException(final Throwable cause) {
        super(cause);
    }
}
