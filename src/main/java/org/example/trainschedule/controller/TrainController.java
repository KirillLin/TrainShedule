package org.example.trainschedule.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.service.TrainService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {
    private final TrainService trainService;

    @GetMapping
    public List<TrainDTO> getAllTrains() {
        return trainService.getAllTrains();
    }

    @GetMapping("/{id}")
    public TrainDTO getTrainById(@PathVariable Long id) {
        return trainService.getTrainById(id);
    }

    @GetMapping("/search")
    public List<TrainDTO> searchTrains(@RequestParam String number) {
        return trainService.searchByNumber(number);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainDTO createTrain(@RequestBody TrainDTO trainDTO) {
        return trainService.createTrain(trainDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
    }

    @GetMapping("/{trainId}/seats")
    public List<SeatDTO> getTrainSeats(@PathVariable Long trainId) {
        return trainService.getTrainSeats(trainId);
    }

    @GetMapping("/{trainId}/seats/filter")
    public List<SeatDTO> getTrainSeatsByType(
            @PathVariable Long trainId,
            @RequestParam String type) {
        return trainService.getTrainSeatsByType(trainId, type);
    }

    @PostMapping("/{trainId}/seats")
    @ResponseStatus(HttpStatus.CREATED)
    public SeatDTO addSeatToTrain(
            @PathVariable Long trainId,
            @RequestBody SeatDTO seatDTO) {
        return trainService.addSeatToTrain(trainId, seatDTO);
    }

    @DeleteMapping("/{trainId}/seats/{seatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSeat(
            @PathVariable Long trainId,
            @PathVariable Long seatId) {
        trainService.deleteSeat(trainId, seatId);
    }
}