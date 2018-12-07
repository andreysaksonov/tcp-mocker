package io.payworks.labs.tcpmocker.support.resource;

import java.io.InputStream;
import java.util.List;

public final class DefaultResourceLoader implements ResourceLoader {

    @Override
    public InputStream getInputStream(final String path) {
        return ResourceUtils.getInputStream(path);
    }

    @Override
    public byte[] toByteArray(final String path) {
        return ResourceUtils.toByteArray(path);
    }

    @Override
    public List<String> list(final String dirPath) {
        return ResourceUtils.listDirectory(dirPath);
    }
}
