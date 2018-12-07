package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Collection;

public interface TcpServerBuilder<T extends TcpServer> {

    TcpServerBuilder<T> withDataHandler(DataHandler dataHandler);

    TcpServerBuilder<T> withDataHandlers(Collection<? extends DataHandler> dataHandlers);

    TcpServerBuilder<T> withPort(int port);

    T build();
}
