package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.datahandler.CompositeDataHandler;
import io.payworks.labs.tcpmocker.datahandler.DataHandlerDispatcherFactory;
import io.payworks.labs.tcpmocker.datahandler.LoggingDataHandler;
import io.payworks.labs.tcpmocker.datahandler.RecordingDataHandler;
import io.payworks.labs.tcpmocker.model.RecordingsRepository;
import io.payworks.labs.tcpmocker.support.DataHandlersLoader;
import io.payworks.labs.tcpmocker.support.DefaultDataHandlersLoader;
import io.payworks.labs.tcpmocker.support.TcpServerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MockServerApp {

    private final MockServerProperties mockServerProperties;

    @Autowired
    public MockServerApp(final MockServerProperties mockServerProperties) {
        this.mockServerProperties = mockServerProperties;
    }

    @Bean
    public DataHandlersLoader dataHandlersLoader() {
        final DefaultDataHandlersLoader dataHandlersLoader = new DefaultDataHandlersLoader();

        dataHandlersLoader.setMappingsPath(mockServerProperties.getTcpMappingsPath());

        return dataHandlersLoader;
    }

    @Bean
    public TcpServer tcpServer(final DataHandlerDispatcherFactory dataHandlerDispatcherFactory) {
        final TcpServerFactory serverFactory = new TcpServerFactory(
                new NettyTcpServerBuilder()
                        .withDataHandlerDispatcherFactory(dataHandlerDispatcherFactory),
                dataHandlersLoader());

        return serverFactory.createTcpServer(mockServerProperties.getPort());
    }

    @Bean
    public DataHandlerDispatcherFactory dataHandlerDispatcherFactory(final RecordingsRepository recordingsRepository) {
        return collection ->
                new LoggingDataHandler(
                        new RecordingDataHandler(recordingsRepository,
                                new CompositeDataHandler(collection)));
    }

    public static void main(final String[] args) {
        SpringApplication.run(MockServerApp.class, args);
    }
}
