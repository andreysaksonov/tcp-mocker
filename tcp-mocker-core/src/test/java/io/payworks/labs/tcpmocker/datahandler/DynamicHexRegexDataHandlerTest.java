package io.payworks.labs.tcpmocker.datahandler;

import org.testng.annotations.Test;

import java.util.Optional;

import static com.google.common.io.BaseEncoding.base16;
import static com.spotify.hamcrest.optional.OptionalMatchers.emptyOptional;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DynamicHexRegexDataHandlerTest {

    private static final String HEX_REGEX = "AABB(\\p{XDigit}{4})CCDD";
    private static final String RESPONSE_DATA_TEMPLATE = "EEFF{0}CCDD";

    private static final byte[] DEFAULT_TEST_DATA = base16().decode("AABB1010CCDD");
    private static final byte[] EMPTY_RESULT_TEST_DATA =  base16().decode("BBBB1010CCDD");

    private final DynamicHexRegexDataHandler dataHandler = new DynamicHexRegexDataHandler(HEX_REGEX, RESPONSE_DATA_TEMPLATE);

    @Test
    public void testHandle() {
        final Optional<byte[]> handleResult = dataHandler.handle(DEFAULT_TEST_DATA);

        assertThat(handleResult, is(optionalWithValue(equalTo(base16().decode("EEFF1010CCDD")))));
    }

    @Test
    public void testHandle_emptyResult() {
        final Optional<byte[]> handleResult = dataHandler.handle(EMPTY_RESULT_TEST_DATA);

        assertThat(handleResult, emptyOptional());
    }
}