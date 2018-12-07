package io.payworks.labs.tcpmocker.datahandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static com.google.common.io.BaseEncoding.base16;

public class LoggingDataHandler implements DataHandler {

    private static final Logger logger = LoggerFactory.getLogger(DataHandler.class);

    private final DataHandler delegate;

    public LoggingDataHandler(final DataHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        final Optional<byte[]> response = delegate.handle(data);

        final String encodedData = base16().encode(data);

        if (response.isPresent()) {
            logger.trace("--> {}", encodedData);
            logger.trace("<-- {}", base16().encode(response.get()));
        } else {
            logger.warn("!!! Bad Match (HEX) (Fragmentation?): {}", encodedData);
        }

        return response;
    }
}
