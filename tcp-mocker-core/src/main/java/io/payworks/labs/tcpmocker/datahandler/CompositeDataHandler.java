package io.payworks.labs.tcpmocker.datahandler;

import java.util.*;
import java.util.stream.Collectors;

public final class CompositeDataHandler implements DataHandler {

    private final List<? extends DataHandler> dataHandlers;

    public CompositeDataHandler(final Collection<? extends DataHandler> dataHandlers) {
        this.dataHandlers = dataHandlers.stream()
                .map(dataHandler -> {
                    if (dataHandler instanceof OrderedDataHandler) {
                        return dataHandler;
                    } else {
                        return new OrderedDataHandler(dataHandler);
                    }
                })
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        for (final DataHandler dataHandler : dataHandlers) {
            final Optional<byte[]> handleResult = dataHandler.handle(data);

            if (handleResult.isPresent()) {
                return handleResult;
            }
        }

        return Optional.empty();
    }
}
