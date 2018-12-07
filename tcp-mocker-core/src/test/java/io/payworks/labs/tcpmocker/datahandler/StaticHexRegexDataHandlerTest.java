package io.payworks.labs.tcpmocker.datahandler;

import org.testng.annotations.Test;

import java.util.Optional;

import static com.google.common.io.BaseEncoding.base16;
import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class StaticHexRegexDataHandlerTest {

    private static final String HEX_REGEX = "74657374.*";
    private static final String RESPONSE_DATA = "FFEE";

    private static final byte[] DEFAULT_TEST_DATA = base16().decode("74657374AABB");
    private static final byte[] EMPTY_RESULT_TEST_DATA = base16().decode("AABB74657374");

    private final StaticHexRegexDataHandler dataHandler = new StaticHexRegexDataHandler(HEX_REGEX, RESPONSE_DATA);

    @Test
    public void testHandle() {
        final Optional<byte[]> handleResult = dataHandler.handle(DEFAULT_TEST_DATA);

        assertThat(handleResult, is(optionalWithValue(equalTo(base16().decode(RESPONSE_DATA)))));
    }

    @Test
    public void testHandle_emptyResult() {
        final Optional<byte[]> handleResult = dataHandler.handle(EMPTY_RESULT_TEST_DATA);

        assertThat(handleResult, emptyOptional());
    }
}