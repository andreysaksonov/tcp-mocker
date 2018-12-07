package io.payworks.labs.tcpmocker.controller;

import io.payworks.labs.tcpmocker.data.RecordingData;
import io.payworks.labs.tcpmocker.service.RecordingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recordings")
public class RecordingsController {

    private final RecordingService recordingService;

    public RecordingsController(final RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @GetMapping(path = "last", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RecordingData> getLastRecording() {
        return recordingService.getLastRecording()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<RecordingData> getAllRecordings(@RequestParam("page") int page,
                                                @RequestParam("size") int size) {
        return recordingService.getAllRecordingsInDescendingOrderByPage(
                page,
                size);
    }
}
