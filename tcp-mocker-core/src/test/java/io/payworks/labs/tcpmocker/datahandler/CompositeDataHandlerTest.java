package io.payworks.labs.tcpmocker.datahandler;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;

import java.util.Optional;

import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CompositeDataHandlerTest {

    private static final byte[] REQUEST_DATA = {7, 6, 5, 4};

    private static final byte[] RESPONSE_DATA_1 = {3, 2, 1, 0};
    private static final byte[] RESPONSE_DATA_2 = {-1, -2, -3, -4};
    private static final byte[] RESPONSE_DATA_3 = {-5, -6, -7, -8};

    @Test
    public void testWithGenericDataHandlers() {
        final CompositeDataHandler dataHandler = new CompositeDataHandler(
                ImmutableList.of(
                        data -> Optional.empty(),
                        data -> Optional.of(RESPONSE_DATA_1),
                        data -> Optional.of(RESPONSE_DATA_2),
                        data -> Optional.of(RESPONSE_DATA_3)
                )
        );

        final Optional<byte[]> handleResult = dataHandler.handle(REQUEST_DATA);

        assertThat(handleResult, is(optionalWithValue(equalTo(RESPONSE_DATA_1))));
    }

    @Test
    public void testWithOrderedDataHandlers() {
        final CompositeDataHandler dataHandler = new CompositeDataHandler(
                ImmutableList.<DataHandler>of(
                        data -> Optional.empty(),
                        data -> Optional.of(RESPONSE_DATA_1),
                        new OrderedDataHandler(-1, data -> Optional.of(RESPONSE_DATA_2)),
                        new OrderedDataHandler(-2, data -> Optional.of(RESPONSE_DATA_3))
                )
        );

        final Optional<byte[]> handleResult = dataHandler.handle(REQUEST_DATA);

        assertThat(handleResult, is(optionalWithValue(equalTo(RESPONSE_DATA_3))));
    }
}