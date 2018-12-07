## tcp-mocker

### Build

`./mvnw clean package`

### Usage

##### Maven:

```
<dependency>
    <groupId>io.payworks.labs.tcpmocker</groupId>
    <artifactId>tcp-mocker-all</artifactId>
    <version>LOCAL-SNAPSHOT</version>
</dependency>
```

##### Java:

```
TcpServerBuilder<? extends TcpServer> serverBuilder = new NettyTcpServerBuilder();
DataHandlersLoader dataHandlersLoader = new JsonMappingDataHandlersLoader();

TcpServerFactory serverFactory = new TcpServerFactory(serverBuilder, dataHandlersLoader);

serverFactory.createTcpServer(10001);
```

##### Docker:

```
docker run -it --rm \
  -p 10001:10001 \
  -v ./tcp-mappings:/var/lib/tcp-mocker/tcp-mappings \
  tcp-mocker-app:LOCAL-SNAPSHOT
```


##### Upgrade Maven:

```
mvn -N io.takari:maven:wrapper -Dmaven=3.6.0
```

##### Tips & Tricks:

    $ docker run -it --rm \
        -p 10001:10001 \
        -v ./tcp-mocker-app/test/resources/tcp-mappings:/var/lib/tcp-mocker/tcp-mappings \
        tcp-mocker-app:LOCAL-SNAPSHOT

    $ echo -ne 'ping' | xxd -p
    70696e67
    
    $ echo -ne '\x70\x69\x6e\x67' | xxd -p
    70696e67
    
    $ echo -ne '\x70\x69\x6e\x67' | nc localhost 10001
    pong
