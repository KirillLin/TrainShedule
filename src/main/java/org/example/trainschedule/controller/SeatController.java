package org.example.trainschedule.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Validated
public class SeatController {
    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatDTO>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByTrainId(@PathVariable Long trainId) {
        return ResponseEntity.ok(seatService.getSeatsByTrainId(trainId));
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @PostMapping("/{trainId}")
    public ResponseEntity<SeatDTO> addSeat(
            @PathVariable Long trainId,
            @Valid @RequestBody SeatDTO seatDTO) {
        seatDTO.setTrainId(trainId);
        SeatDTO createdSeat = seatService.addSeatToTrain(trainId, seatDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeat);
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @PatchMapping("/{id}")
    public ResponseEntity<SeatDTO> updateSeat(
            @PathVariable Long id,
            @Valid @RequestBody SeatDTO seatDTO) {
        return ResponseEntity.ok(seatService.updateSeat(id, seatDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}