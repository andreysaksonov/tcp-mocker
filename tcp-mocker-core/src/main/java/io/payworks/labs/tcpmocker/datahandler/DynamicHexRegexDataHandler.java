package io.payworks.labs.tcpmocker.datahandler;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.regex.Matcher;

import static com.google.common.io.BaseEncoding.base16;

public final class DynamicHexRegexDataHandler extends HexRegexDataHandler {

    private final String responseDataTemplate;

    public DynamicHexRegexDataHandler(final String hexRegex,
                                      final String responseDataTemplate) {
        super(hexRegex);
        this.responseDataTemplate = responseDataTemplate;
    }

    @Override
    public Optional<byte[]> handle(final byte[] data) {
        final String encoded = base16().encode(data);
        final Matcher regexMatcher = hexRegex.matcher(encoded);
        final boolean matches = regexMatcher.matches();

        if (matches) {
            final int groupCount = regexMatcher.groupCount();

            if (groupCount == 0) {
                return Optional.of(base16().decode(MessageFormat.format(responseDataTemplate, regexMatcher.group())));
            }

            final Object[] groups = new Object[groupCount];
            for (int i = 0; i < groupCount; i++) {
                groups[i] = regexMatcher.group(i + 1);
            }
            return Optional.of(base16().decode(MessageFormat.format(responseDataTemplate, groups)));
        } else {
            return Optional.empty();
        }
    }
}
