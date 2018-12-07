package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractTcpServerBuilder<SERVER extends TcpServer, BUILDER extends AbstractTcpServerBuilder<SERVER, BUILDER>> implements TcpServerBuilder<SERVER> {

    private final Collection<DataHandler> dataHandlers = new ArrayList<>();
    private int port;

    protected abstract BUILDER self();

    @Override
    public BUILDER withDataHandler(final DataHandler dataHandler) {
        this.dataHandlers.add(dataHandler);
        return self();
    }

    @Override
    public BUILDER withDataHandlers(final Collection<? extends DataHandler> dataHandlers) {
        this.dataHandlers.addAll(dataHandlers);
        return self();
    }

    @Override
    public BUILDER withPort(final int port) {
        this.port = port;
        return self();
    }

    protected Collection<? extends DataHandler> getDataHandlers() {
        return dataHandlers;
    }

    int getPort() {
        return port;
    }
}
