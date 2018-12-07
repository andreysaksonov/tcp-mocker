package io.payworks.labs.tcpmocker.support;

import com.google.common.collect.ImmutableMap;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModelReader;
import io.payworks.labs.tcpmocker.support.json.JsonMappingReader;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.google.common.io.BaseEncoding.base16;
import static com.spotify.hamcrest.optional.OptionalMatchers.optionalWithValue;
import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWithIgnoringCase;

public class DefaultDataHandlersLoaderTest {

    private static final byte[] REQUEST_TEST_DATA = base16().decode("020012345678904F2F0F90000030");

    @DataProvider
    public Object[][] testJsonMappings() {
        return new Object[][] {
                {TEST_DEFAULT_JSON_MAPPING_1, "ad122e1b75356c6fdf3e9c3076a80da6"},
                {TEST_DYNAMIC_JSON_MAPPING_1, "020012345678904F2F0F90000030"},
                {TEST_DYNAMIC_JSON_MAPPING_2, "123456789042FA0000"}
        };
    }

    @Test(dataProvider = "testJsonMappings")
    public void testDataHandlerLoading(final String filePath,
                                       final String expectedResponseDataPrefix) {
        testDataHandlerLoading(new JsonMappingReader(), filePath, expectedResponseDataPrefix);
    }

    private static void testDataHandlerLoading(final DataHandlerModelReader dataHandlerModelReader,
                                               final String filePath,
                                               final String expectedResponseDataPrefix) {
        final DataHandler dataHandler = loadDataHandler(dataHandlerModelReader, filePath);

        final Optional<byte[]> handleResult = dataHandler.handle(REQUEST_TEST_DATA);

        assertThat(handleResult.map(base16()::encode), is(optionalWithValue(startsWithIgnoringCase(expectedResponseDataPrefix))));
    }

    private static DataHandler loadDataHandler(final DataHandlerModelReader dataHandlerModelReader,
                                               final String filePath) {
        final DefaultDataHandlersLoader dataHandlersLoader = new DefaultDataHandlersLoader();

        dataHandlersLoader.setMappingsPath(Paths.get(filePath).getParent().toString());
        dataHandlersLoader.setReadersMap(ImmutableMap.of(
                Pattern.compile(filePath, Pattern.CASE_INSENSITIVE), dataHandlerModelReader
        ));

        return dataHandlersLoader.dataHandlers().get(filePath);
    }
}