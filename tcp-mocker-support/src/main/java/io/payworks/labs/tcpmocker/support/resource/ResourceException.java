package io.payworks.labs.tcpmocker.support.resource;

public class ResourceException extends RuntimeException {

    public ResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceException(Throwable cause) {
        super(cause);
    }
}
