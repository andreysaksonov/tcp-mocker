package io.payworks.labs.tcpmocker.model;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackageClasses = Recording.class)
@EnableJpaRepositories(basePackageClasses = RecordingsRepository.class)
@SpringBootConfiguration
public class TestConfiguration {
}
