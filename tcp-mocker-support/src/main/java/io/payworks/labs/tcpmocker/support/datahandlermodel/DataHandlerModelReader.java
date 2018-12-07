package io.payworks.labs.tcpmocker.support.datahandlermodel;

import java.io.InputStream;
import java.net.URL;

public interface DataHandlerModelReader {

    DataHandlerModel read(InputStream src);

    DataHandlerModel read(URL src);
}
