package io.payworks.labs.tcpmocker;

public abstract class AbstractTcpServer implements TcpServer {

    private final int port;

    public AbstractTcpServer(int port) {
        this.port = port;
    }

    @Override
    public int getPort() {
        return port;
    }
}
