package io.payworks.labs.tcpmocker.datahandler;

import io.payworks.labs.tcpmocker.model.Recording;
import io.payworks.labs.tcpmocker.model.RecordingsRepository;
import org.apache.commons.lang3.RandomUtils;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.MockitoAnnotations.initMocks;

public class RecordingDataHandlerTest {

    @Mock
    private RecordingsRepository repository;

    @Mock
    private DataHandler delegate;

    @InjectMocks
    private RecordingDataHandler victim;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
    }

    @AfterMethod
    public void tearDown() {
        this.victim = null;
    }

    @Test
    public void handleSavesRecordingIfDelegateSuccessfullyHandlesGivenData() {
        final byte[] givenData = RandomUtils.nextBytes(100);
        final byte[] givenResult = RandomUtils.nextBytes(50);
        final Recording givenRecording = Recording.builder()
                .withRequest(givenData)
                .withReply(givenResult)
                .build();

        given(delegate.handle(any())).willReturn(Optional.of(givenResult));
        given(repository.save(any())).willReturn(givenRecording);

        final Optional<byte[]> result = victim.handle(givenData);

        assertThat(result).hasValue(givenResult);
        thenRepositoryShouldHaveSavedTheRecording(givenData, givenResult);
    }

    private void thenRepositoryShouldHaveSavedTheRecording(final byte[] givenRequest, final byte[] givenReply) {
        final ArgumentCaptor<Recording> conversationCaptor = ArgumentCaptor.forClass(Recording.class);
        then(repository).should().save(conversationCaptor.capture());
        assertThat(conversationCaptor.getValue())
                .extracting(
                        Recording::getRequest,
                        Recording::getReply)
                .contains(
                        givenRequest,
                        givenReply);
    }

    @Test
    public void handleRecordsNothingIfDelegateFailsToHandleGivenData() {
        final byte[] givenData = RandomUtils.nextBytes(100);
        given(delegate.handle(any())).willReturn(Optional.empty());

        final Optional<byte[]> result = victim.handle(givenData);

        assertThat(result).isEmpty();
        then(repository).shouldHaveZeroInteractions();
    }
}