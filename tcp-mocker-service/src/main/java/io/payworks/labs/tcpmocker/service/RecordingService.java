package io.payworks.labs.tcpmocker.service;

import io.payworks.labs.tcpmocker.data.RecordingData;
import io.payworks.labs.tcpmocker.model.RecordingsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordingService {

    private final RecordingsRepository recordingsRepository;

    public RecordingService(final RecordingsRepository recordingsRepository) {
        this.recordingsRepository = recordingsRepository;
    }

    public Optional<RecordingData> getLastRecording() {
        return recordingsRepository.findTopByOrderByEventTimeDesc()
                .map(RecordingService::from);
    }

    public List<RecordingData> getAllRecordingsInDescendingOrderByPage(final int pageNumber, final int size) {
        return recordingsRepository.findAllByOrderByEventTimeDesc(PageRequest.of(pageNumber, size))
                .stream()
                .map(RecordingService::from)
                .collect(Collectors.toList());
    }

    private static RecordingData from(final io.payworks.labs.tcpmocker.model.Recording recording) {
        return RecordingData.create()
                .withTimestamp(recording.getEventTime())
                .withRequest(recording.getRequest())
                .withReply(recording.getReply())
                .build();
    }
}
