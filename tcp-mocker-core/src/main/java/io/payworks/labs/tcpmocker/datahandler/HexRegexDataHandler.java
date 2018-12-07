package io.payworks.labs.tcpmocker.datahandler;

import java.util.regex.Pattern;

public abstract class HexRegexDataHandler implements DataHandler {

    protected final Pattern hexRegex;

    protected HexRegexDataHandler(final String hexRegex) {
        this.hexRegex = Pattern.compile(hexRegex, Pattern.CASE_INSENSITIVE);
    }

    public Pattern hexRegex() {
        return hexRegex;
    }
}
