package io.payworks.labs.tcpmocker;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("mockserver")
public class MockServerProperties {

    private int port;
    private String tcpMappingsPath;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTcpMappingsPath() {
        return tcpMappingsPath;
    }

    public void setTcpMappingsPath(String tcpMappingsPath) {
        this.tcpMappingsPath = tcpMappingsPath;
    }
}
