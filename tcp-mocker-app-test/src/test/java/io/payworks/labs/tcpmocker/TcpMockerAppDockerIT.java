package io.payworks.labs.tcpmocker;

import io.payworks.labs.tcpmocker.test.tcpclient.SimpleTcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testng.annotations.*;

import java.io.File;
import java.util.Optional;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TcpMockerAppDockerIT {

    private static final Logger logger = LoggerFactory.getLogger(TcpMockerAppDockerIT.class);
    private static final Logger dockerTcpMockerAppLogger = LoggerFactory.getLogger("tcp-mocker-app");

    private static final String TCP_MOCKER_APP_SERVICE_NAME = "tcp-mocker-app";

    private static final int TCP_SERVICE_PORT = 10001;
    private static final int WEB_SERVICE_PORT = 8080;

    private static final String TCP_MOCKER_APP_CONTAINER_TAG = Optional.ofNullable(System.getenv("PROJECT_VERSION"))
            .orElse("LOCAL-SNAPSHOT");

    private static final DockerComposeContainer<?> tcpMockerAppDockerCompose =
            new DockerComposeContainer<>(new File("tcp-mocker-app/docker-compose.yml"))
                    .withExposedService(TCP_MOCKER_APP_SERVICE_NAME, TCP_SERVICE_PORT)
                    .withExposedService(TCP_MOCKER_APP_SERVICE_NAME, WEB_SERVICE_PORT)
                    .withEnv("TCP_MOCKER_APP_TAG", TCP_MOCKER_APP_CONTAINER_TAG)
                    .withLocalCompose(true)
                    .withPull(false)
                    .withTailChildContainers(true)
                    .withLogConsumer("tcp-mocker-app", new Slf4jLogConsumer(dockerTcpMockerAppLogger))
                    ;

    private SimpleTcpClient tcpClient;

    @BeforeClass
    public static void startContainer() {
        tcpMockerAppDockerCompose.start();
    }

    @AfterClass
    public static void stopContainer() {
        tcpMockerAppDockerCompose.stop();
    }

    @BeforeMethod
    public void setup() throws Exception {
        tcpClient = new SimpleTcpClient(getTcpServiceHost(), getTcpServicePort());
    }

    @AfterMethod
    public void close() throws Exception {
        tcpClient.close();
    }

    @Test
    public void testTcpMockerApp() throws Exception {
        final String requestPayload = "ping";
        final byte[] requestBytes = requestPayload.getBytes(UTF_8);

        final byte[] receivedBytes = tcpClient.sendAndReceive(requestBytes);
        final String receivedPayload = new String(receivedBytes, UTF_8);

        final String expectedPayload = "pong";
        assertThat(receivedPayload, equalTo(expectedPayload));
    }

    private static Integer getTcpServicePort() {
        return tcpMockerAppDockerCompose.getServicePort(TCP_MOCKER_APP_SERVICE_NAME, TCP_SERVICE_PORT);
    }

    private static String getTcpServiceHost() {
        return tcpMockerAppDockerCompose.getServiceHost(TCP_MOCKER_APP_SERVICE_NAME, TCP_SERVICE_PORT);
    }
}
