package io.payworks.labs.tcpmocker.datahandler;

import com.google.common.io.BaseEncoding;

import java.util.Optional;

import static com.google.common.io.BaseEncoding.base16;

public final class StaticHexRegexDataHandler extends HexRegexDataHandler {

    private final String responseData;

    public StaticHexRegexDataHandler(final String hexRegex,
                                     final String responseData) {
        super(hexRegex);
        this.responseData = responseData;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        final String encoded = BaseEncoding.base16().encode(data);
        final boolean matches = hexRegex.matcher(encoded).matches();

        if (matches) {
            return Optional.of(base16().decode(responseData.toUpperCase()));
        } else {
            return Optional.empty();
        }
    }
}
