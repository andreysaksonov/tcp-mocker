package io.payworks.labs.tcpmocker.service;

import com.google.common.collect.ImmutableList;
import io.payworks.labs.tcpmocker.data.RecordingData;
import io.payworks.labs.tcpmocker.model.Recording;
import io.payworks.labs.tcpmocker.model.RecordingsRepository;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.groups.Tuple;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.io.BaseEncoding.base16;
import static io.payworks.labs.tcpmocker.util.PageCollectorTemplate.collectAllPages;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class RecordingServiceTest {

    private static final int TESTING_PAGE_SIZE = 3;

    @Mock
    private RecordingsRepository recordingsRepository;

    @InjectMocks
    private RecordingService victim;

    @BeforeMethod
    void setUp() {
        initMocks(this);
    }

    @AfterMethod
    void tearDown() {
        victim = null;
    }

    @Test
    public void getLastRecordingQueriesTheRepositoryForTheLastRecording() {
        final Recording givenRecording = givenRecording();
        given(recordingsRepository.findTopByOrderByEventTimeDesc()).willReturn(Optional.of(givenRecording));

        final Optional<RecordingData> recordingData = victim.getLastRecording();

        assertThat(recordingData.isPresent())
                .isTrue();
        assertThat(recordingData.get())
                .extracting(
                        RecordingData::getTimestamp,
                        RecordingData::getRequest,
                        RecordingData::getReply)
                .contains(
                        givenRecording.getEventTime(),
                        base16().encode(givenRecording.getRequest()),
                        base16().encode(givenRecording.getReply()));
    }

    private Recording givenRecording() {
        return Recording.builder()
                .withRequest(RandomUtils.nextBytes(100))
                .withReply(RandomUtils.nextBytes(10))
                .build();
    }

    @Test
    public void getLastReturnsEmptyIfRepositoryIsEmpty() {
        given(recordingsRepository.findTopByOrderByEventTimeDesc()).willReturn(Optional.empty());

        final Optional<RecordingData> recordingData = victim.getLastRecording();

        assertThat(recordingData).isEmpty();
    }

    @Test
    public void getAllRecordingsQueryCollectsThemFromTheRepository() {
        final List<Recording> page1 = givenSomeRecordings(10);
        final List<Recording> page2 = givenSomeRecordings(10);
        final List<Recording> page3 = givenSomeRecordings(10);
        given(recordingsRepository.findAllByOrderByEventTimeDesc(any(Pageable.class)))
                .willReturn(page1, page2, page3, ImmutableList.of());

        final List<RecordingData> recordingData = collectAllPages(victim::getAllRecordingsInDescendingOrderByPage, TESTING_PAGE_SIZE);

        thenAllRecordingsHaveBeenCollected(recordingData, page1, page2, page3);
    }

    private List<Recording> givenSomeRecordings(final int length) {
        return Stream.generate(this::givenRecording).limit(length).collect(Collectors.toList());
    }

    private void thenAllRecordingsHaveBeenCollected(final List<RecordingData> recordingData, final List<Recording>... pages) {
        assertThat(recordingData)
                .isNotEmpty()
                .extracting(
                        RecordingData::getTimestamp,
                        RecordingData::getRequest,
                        RecordingData::getReply)
                .contains(
                        Arrays.stream(pages).flatMap(Collection::stream)
                                .collect(Collectors.toList())
                                .stream()
                                .map(rec -> tuple(
                                        rec.getEventTime(),
                                        base16().encode(rec.getRequest()),
                                        base16().encode(rec.getReply())))
                                .toArray(Tuple[]::new));

    }

    @Test
    public void getAllRecordingsReturnsAnEmptyListIfTheRepositoryIsEmpty() {
        given(recordingsRepository.findAll()).willReturn(ImmutableList.of());

        final List<RecordingData> recordingData = collectAllPages(victim::getAllRecordingsInDescendingOrderByPage, TESTING_PAGE_SIZE);

        assertThat(recordingData).isEmpty();
    }
}