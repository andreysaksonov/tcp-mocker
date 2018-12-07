package io.payworks.labs.tcpmocker.support.resource;

import java.io.InputStream;
import java.util.List;

public interface ResourceLoader {

    InputStream getInputStream(String path);

    byte[] toByteArray(String path);

    List<String> list(String dirPath);
}
