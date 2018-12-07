package io.payworks.labs.tcpmocker.support;

import io.payworks.labs.tcpmocker.AbstractTcpServerBuilder;
import io.payworks.labs.tcpmocker.TcpServer;
import io.payworks.labs.tcpmocker.datahandler.DataHandler;
import org.hamcrest.Matcher;
import org.testng.annotations.Test;

import java.util.Collection;

import static io.payworks.labs.tcpmocker.test.TcpMappingsRegistry.TEST_MAPPINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class TcpServerFactoryTest {

    private static final class TestTcpServer implements TcpServer {

        private TestTcpServer(final Collection<? extends DataHandler> dataHandlers,
                              final Matcher<Collection<? extends DataHandler>> collectionAssertion) {
            assertThat(dataHandlers, collectionAssertion);
        }

        @Override
        public int getPort() {
            return 0;
        }

        @Override
        public void close() {
        }
    }

    private static final class TestTcpServerBuilder extends AbstractTcpServerBuilder<TestTcpServer, TestTcpServerBuilder> {

        private final Matcher<Collection<? extends DataHandler>> responsesAssertion;

        TestTcpServerBuilder(final Matcher<Collection<? extends DataHandler>> collectionAssertion) {
            this.responsesAssertion = collectionAssertion;
        }

        @Override
        protected TestTcpServerBuilder self() {
            return this;
        }

        @Override
        public TestTcpServer build() {
            return new TestTcpServer(getDataHandlers(), responsesAssertion);
        }
    }

    @Test
    public void testDataHandlersLoading() {
        final DefaultDataHandlersLoader dataHandlersLoader = new DefaultDataHandlersLoader();

        final TcpServerFactory tcpServerFactory = new TcpServerFactory(new TestTcpServerBuilder(hasSize(TEST_MAPPINGS.size())), dataHandlersLoader);

        tcpServerFactory.createTcpServer(0);
    }
}