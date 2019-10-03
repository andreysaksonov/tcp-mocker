package io.payworks.labs.tcpmocker.recording;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@EntityScan(basePackageClasses = RecordingEntity.class)
@EnableJdbcRepositories(basePackageClasses = RecordingsRepository.class)
@SpringBootConfiguration
public class RecordingsTestConfiguration {
}
