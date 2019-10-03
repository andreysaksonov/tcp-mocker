package io.payworks.labs.tcpmocker.recording;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Immutable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Arrays;

@Immutable
@Table("Recording")
public class RecordingEntity {

    @Id
    private Long id;

    @Column
    private Instant timestamp;

    @Column
    private byte[] request;

    @Column
    private byte[] reply;

    static RecordingEntity of(final byte[] request,
                              final byte[] reply) {
        return new RecordingEntity(null, Instant.now(), request, reply);
    }

    RecordingEntity(final Long id,
                    final Instant timestamp,
                    final byte[] request,
                    final byte[] reply) {
        this.id = id;
        this.timestamp = timestamp;
        this.request = request;
        this.reply = reply;
    }

    RecordingEntity withId(Long id) {
        return new RecordingEntity(id, this.timestamp, this.request, this.reply);
    }

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public byte[] getRequest() {
        return request;
    }

    public byte[] getReply() {
        return reply;
    }

    @Override
    public String toString() {
        return "RecordingEntity{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", request=" + Arrays.toString(request) +
                ", reply=" + Arrays.toString(reply) +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private byte[] request;
        private byte[] reply;

        private Builder() {
        }

        public Builder withRequest(final byte[] request) {
            this.request = request;
            return this;
        }

        public Builder withReply(final byte[] reply) {
            this.reply = reply;
            return this;
        }

        public RecordingEntity build() {
            final RecordingEntity recordingEntity = RecordingEntity.of(request, reply);

            this.request = null;
            this.reply = null;

            return recordingEntity;
        }
    }
}
