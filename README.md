[![Build Status](https://travis-ci.com/payworks/tcp-mocker.svg?branch=master)](https://travis-ci.com/payworks/tcp-mocker)

## tcp-mocker

### Build

##### Maven

`./mvnw clean package`


##### Docker

`docker-compose up`

### Usage

##### Maven:

```
<dependency>
    <groupId>io.payworks.labs.tcpmocker</groupId>
    <artifactId>tcp-mocker-service</artifactId>
    <version>LOCAL-SNAPSHOT</version>
</dependency>
```

##### Java:

```
var port = 10001;

var dataHandlersLoader = new DefaultDataHandlersLoader();
dataHandlersLoader.setMappingsPath("./tcp-mappings")

var serverBuilder = new NettyTcpServerBuilder();
var serverFactory = new TcpServerFactory(serverBuilder, dataHandlersLoader);

serverFactory.createTcpServer(port);
```

##### Docker:

```
docker run -it --rm \
  -p 10001:10001 \
  -v $(pwd)/tcp-mappings:/var/lib/tcp-mocker/tcp-mappings \
  tcp-mocker-app:LOCAL-SNAPSHOT
```


### Tips & Tricks

##### Upgrade Maven Version

```
mvn -N io.takari:maven:wrapper -Dmaven=3.6.0
```

##### TCP Mocker Application Playground

    $ docker run -it --rm \
        -p 10001:10001 \
        -v $(pwd)/tcp-mocker-app-test/tcp-mocker-app/tcp-mappings:/var/lib/tcp-mocker/tcp-mappings \
        tcp-mocker-app:LOCAL-SNAPSHOT

    $ echo -ne 'ping' | xxd -p
    70696e67
    
    $ echo -ne '\x70\x69\x6e\x67' | xxd -p
    70696e67
    
    $ echo -ne '\x70\x69\x6e\x67' | ncat localhost 10001
    pong

    $ echo -ne '\x70\x69\x6e\x67' | ncat localhost 10001 | xxd -p
    706f6e67
