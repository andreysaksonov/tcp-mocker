package io.payworks.labs.tcpmocker.datahandler;

public class DataHandlerException extends RuntimeException {

    public DataHandlerException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public DataHandlerException(final Throwable cause) {
        super(cause);
    }
}
