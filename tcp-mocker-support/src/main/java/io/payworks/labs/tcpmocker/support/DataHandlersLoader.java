package io.payworks.labs.tcpmocker.support;

import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public interface DataHandlersLoader {

    Map<String, ? extends DataHandler> dataHandlers(Set<String> filter);

    default Map<String, ? extends DataHandler> dataHandlers() {
        return dataHandlers(Collections.emptySet());
    }
}
