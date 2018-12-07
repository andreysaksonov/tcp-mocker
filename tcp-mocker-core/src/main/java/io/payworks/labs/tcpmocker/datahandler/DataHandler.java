package io.payworks.labs.tcpmocker.datahandler;

import java.util.Optional;

public interface DataHandler {

    Optional<byte[]> handle(byte[] data);
}
