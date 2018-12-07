package io.payworks.labs.tcpmocker.support.json;

import io.payworks.labs.tcpmocker.support.datahandlermodel.DataHandlerModel;
import org.testng.annotations.Test;

import java.net.URL;

import static io.payworks.labs.tcpmocker.support.resource.ResourceUtils.toResourceUrl;
import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.TEST_DEFAULT_JSON_MAPPING_1;
import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.TEST_LIST_JSON_MAPPING_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.notNullValue;

public class JsonMappingReaderTest {

    private static final URL TEST_DEFAULT_JSON_MAPPING_1_URL =
            toResourceUrl(TEST_DEFAULT_JSON_MAPPING_1);

    private static final URL TEST_LIST_JSON_MAPPING_1_URL =
            toResourceUrl(TEST_LIST_JSON_MAPPING_1);

    private final JsonMappingReader mappingReader = new JsonMappingReader();

    @Test
    public void readTestDefaultJsonMapping() {
        final DataHandlerModel dataHandlerModel = mappingReader.read(TEST_DEFAULT_JSON_MAPPING_1_URL);
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

    @Test
    public void readTestListJsonMapping() {
        final DataHandlerModel dataHandlerModel = mappingReader.read(TEST_LIST_JSON_MAPPING_1_URL);
        assertThat(dataHandlerModel, notNullValue());

        assertThat(dataHandlerModel.getRequest().getMatches(), equalToIgnoringCase("0200\\d{10}4F2F0F90000030"));
        assertThat(dataHandlerModel.getResponse().getData(), equalToIgnoringCase("ad122e1b75356c6fdf3e9c3076a80da6"));
    }

}