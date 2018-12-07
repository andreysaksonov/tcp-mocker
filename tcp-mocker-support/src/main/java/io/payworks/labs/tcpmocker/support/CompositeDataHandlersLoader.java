package io.payworks.labs.tcpmocker.support;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CompositeDataHandlersLoader implements DataHandlersLoader {

    private final List<DataHandlersLoader> delegateList;

    public CompositeDataHandlersLoader(final List<DataHandlersLoader> delegateList) {
        this.delegateList = delegateList;
    }

    @Override
    public Map<String, ? extends DataHandler> dataHandlers(final Set<String> filter) {
        final ImmutableSet.Builder<String> filterBuilder = ImmutableSet.<String>builder().addAll(filter);
        final ImmutableMap.Builder<String, DataHandler> dataHandlers = ImmutableMap.builder();

        for (final DataHandlersLoader delegate : delegateList) {
            dataHandlers.putAll(delegate.dataHandlers(filterBuilder.build()));
            filterBuilder.addAll(dataHandlers.build().keySet());
        }

        return dataHandlers.build();
    }
}
