package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.CompositeDataHandler;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.datahandler.DataHandlerDispatcherFactory;

abstract class DispatchingDataHandlerServerBuilder<SERVER extends TcpServer, BUILDER extends DispatchingDataHandlerServerBuilder<SERVER, BUILDER>>
        extends AbstractTcpServerBuilder<SERVER, BUILDER> {

    private DataHandlerDispatcherFactory dataHandlerDispatcherFactory = CompositeDataHandler::new;

    public BUILDER withDataHandlerDispatcherFactory(final DataHandlerDispatcherFactory dataHandlerDispatcherFactory) {
        this.dataHandlerDispatcherFactory = dataHandlerDispatcherFactory;
        return self();
    }

    DataHandler createDataHandlerDispatcher() {
        return dataHandlerDispatcherFactory.create(getDataHandlers());
    }
}
