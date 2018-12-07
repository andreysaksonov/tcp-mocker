package io.payworks.labs.tcpmocker.support.json;

public class JsonMappingReaderException extends RuntimeException {

    public JsonMappingReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public JsonMappingReaderException(final Throwable cause) {
        super(cause);
    }
}
