package io.payworks.labs.tcpmocker;

import java.io.Closeable;

public interface TcpServer extends Closeable {
    int getPort();
}
