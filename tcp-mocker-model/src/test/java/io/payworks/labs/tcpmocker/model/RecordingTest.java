package io.payworks.labs.tcpmocker.model;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.groups.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.payworks.labs.tcpmocker.util.PageCollectorTemplate.collectAllPages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@DataJpaTest
public class RecordingTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private RecordingsRepository repository;

    @BeforeMethod
    private void resetRepository() {
        repository.deleteAll();
    }

    @Test
    public void writeRecording() {
        final Recording givenRecording = givenARandomRecording();

        final Recording storedRecording = repository.save(givenRecording);

        assertThat(storedRecording.getId()).isNotNull();
        thenBothRecordingsAreSimilar(storedRecording, givenRecording);
    }

    private Recording givenARandomRecording() {
        final byte[] request = RandomUtils.nextBytes(100);
        final byte[] reply = RandomUtils.nextBytes(50);

        try {
            Thread.sleep(100);
        } catch (final InterruptedException ignored) {
        }

        return Recording.builder()
                .withRequest(request)
                .withReply(reply)
                .build();
    }

    @Test
    public void findLastRecording() {
        final Deque<Recording> givenRecordings = givenSomeRandomRecordings(10);
        repository.saveAll(givenRecordings);

        final Optional<Recording> lastRecording = repository.findTopByOrderByEventTimeDesc();

        assertThat(lastRecording)
                .hasValueSatisfying(recording ->
                        thenBothRecordingsAreSimilar(recording, givenRecordings.getLast()));
    }

    private Deque<Recording> givenSomeRandomRecordings(final int length) {
        return Stream.generate(this::givenARandomRecording).limit(length).collect(Collectors.toCollection(ArrayDeque::new));
    }

    private void thenBothRecordingsAreSimilar(final Recording actual, final Recording expected) {
        assertThat(actual)
                .extracting(
                        Recording::getEventTime,
                        Recording::getRequest,
                        Recording::getReply)
                .contains(
                        expected.getEventTime(),
                        expected.getRequest(),
                        expected.getReply());
    }

    @Test
    public void findAllUsingPagesWorksAsExpected() {
        final Deque<Recording> givenRecordings = givenSomeRandomRecordings(10);
        repository.saveAll(givenRecordings);

        final List<Recording> recordings = collectAllPages(repository::findAllByOrderByEventTimeDesc, PageRequest::of, 3);

        assertThat(recordings)
                .extracting(
                        Recording::getEventTime,
                        Recording::getRequest,
                        Recording::getReply)
                .contains(
                        givenRecordings.stream()
                                .map(rec -> tuple(
                                        rec.getEventTime(),
                                        rec.getRequest(),
                                        rec.getReply()))
                                .toArray(Tuple[]::new));
    }
}
