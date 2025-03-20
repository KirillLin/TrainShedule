package org.example.trainschedule.controller;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.TrainSchedule;
import org.example.trainschedule.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SuppressWarnings("checkstyle:RegexpMultiline")
@RestController
@RequestMapping("/trains")
public class TrainScheduleController {

    private final TrainScheduleService trainScheduleService;

    @Autowired
    public TrainScheduleController(TrainScheduleService trainScheduleService) {
        this.trainScheduleService = trainScheduleService;
    }

    @GetMapping
    public ResponseEntity<List<TrainSchedule>> searchTrains(
            @RequestParam(required = false) String departureStation,
            @RequestParam(required = false) String arrivalStation) {
        List<TrainSchedule> trains = trainScheduleService.searchTrains(departureStation, arrivalStation);
        return new ResponseEntity<>(trains, HttpStatus.OK);
    }

    @GetMapping("/{trainNumber}")
    public ResponseEntity<TrainSchedule> getTrainByNumber(@PathVariable String trainNumber) {
        Optional<TrainSchedule> trainSchedule = trainScheduleService.getTrainByNumber(trainNumber);
        return trainSchedule.map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @SuppressWarnings("checkstyle:Indentation")
    @PostMapping
    public ResponseEntity<TrainSchedule> createTrainSchedule(@RequestBody TrainSchedule trainSchedule) {
         trainSchedule = trainScheduleService.createTrainSchedule(trainSchedule);
        return new ResponseEntity<>(trainSchedule, HttpStatus.CREATED);
    }

    @PutMapping("/{trainNumber}")
    public ResponseEntity<TrainSchedule> updateTrainSchedule(
            @PathVariable String trainNumber,
            @RequestBody TrainSchedule trainSchedule) {
        try {
            TrainSchedule updatedTrain = trainScheduleService.updateTrainSchedule(trainNumber, trainSchedule);
            return new ResponseEntity<>(updatedTrain, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{trainNumber}")
    public ResponseEntity<Void> deleteTrainSchedule(@PathVariable String trainNumber) {
        trainScheduleService.deleteTrainSchedule(trainNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}