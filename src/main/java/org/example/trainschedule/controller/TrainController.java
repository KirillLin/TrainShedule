package org.example.trainschedule.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {
    private final TrainService trainService;

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @PostMapping
    public ResponseEntity<TrainDTO> createTrain(@Valid @RequestBody TrainDTO trainDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainService.createTrain(trainDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrainById(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainDTO>> searchTrains(@RequestParam String number) {
        return ResponseEntity.ok(trainService.searchByNumber(number));
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @PutMapping("/by-number/{trainNumber}")
    public ResponseEntity<TrainDTO> updateTrainByNumber(
            @PathVariable String trainNumber,
            @Valid @RequestBody TrainDTO trainDTO) {
        return ResponseEntity.ok(trainService.updateTrainByNumber(trainNumber, trainDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/with-free-seats")
    public ResponseEntity<List<TrainDTO>> getTrainsWithFreeSeats(
            @RequestParam String departure,
            @RequestParam String arrival) {
        return ResponseEntity.ok(trainService.findTrainsWithFreeSeats(departure, arrival));
    }
}