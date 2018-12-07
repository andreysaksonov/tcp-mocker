package io.payworks.labs.tcpmocker;

public class TcpServerException extends RuntimeException {

    public TcpServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TcpServerException(Throwable cause) {
        super(cause);
    }
}
