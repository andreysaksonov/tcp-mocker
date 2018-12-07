package io.payworks.labs.tcpmocker.datahandler;

import java.util.Collection;

public interface DataHandlerDispatcherFactory {

    DataHandler create(Collection<? extends DataHandler> dataHandlers);
}
