package io.payworks.labs.tcpmocker.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.Arrays;

@Entity
public class Recording {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Instant eventTime;

    @Column(nullable = false, length = 8192)
    private byte[] request;

    @Column(nullable = false, length = 8192)
    private byte[] reply;

    protected Recording() {
        // Intentionally empty, required by JPA-spec.
    }

    private Recording(final byte[] request,
                      final byte[] reply) {
        this.eventTime = Instant.now();
        this.request = request;
        this.reply = reply;
    }

    public Long getId() {
        return id;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public byte[] getRequest() {
        return request;
    }

    public byte[] getReply() {
        return reply;
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id=" + id +
                ", eventTime=" + eventTime +
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

        public Recording build() {
            final Recording recording = new Recording(request, reply);
            this.request = null;
            this.reply = null;

            return recording;
        }
    }
}
