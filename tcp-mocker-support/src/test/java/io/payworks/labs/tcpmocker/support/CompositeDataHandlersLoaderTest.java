package io.payworks.labs.tcpmocker.support;

import com.google.common.collect.ImmutableList;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import org.testng.annotations.Test;

import java.util.Map;

import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.TEST_MAPPINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CompositeDataHandlersLoaderTest {

    @Test
    public void testDataHandlersLoading() {
        final CompositeDataHandlersLoader dataHandlersLoader = new CompositeDataHandlersLoader(
                ImmutableList.of(
                        new DefaultDataHandlersLoader(),
                        new DefaultDataHandlersLoader(),
                        new DefaultDataHandlersLoader()
                )
        );

        final Map<String, ? extends DataHandler> dataHandlersMap = dataHandlersLoader.dataHandlers();

        assertThat(dataHandlersMap.keySet(), equalTo(TEST_MAPPINGS));
    }
}