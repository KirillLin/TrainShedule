package org.example.trainschedule.controller;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.domain.TrainSchedule;
import org.example.trainschedule.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trains")
public class TrainScheduleController {
    private final TrainScheduleService trainScheduleService;

    @Autowired
    public TrainScheduleController(TrainScheduleService trainScheduleService) {
        this.trainScheduleService = trainScheduleService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainSchedule>> searchTrains(
            @RequestParam(value = "departure", required = false) String departureStation,
            @RequestParam(value = "arrival", required = false) String arrivalStation) {
        List<TrainSchedule> results = trainScheduleService.searchTrains(departureStation, arrivalStation);
        if (results.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return 404 if no results
        } else {
            return new ResponseEntity<>(results, HttpStatus.OK); // Return results with 200 OK
        }
    }

    @SuppressWarnings("checkstyle:LineLength")
    @GetMapping("/{trainId}")
    public ResponseEntity<TrainSchedule> getTrainById(@PathVariable String trainId) {
        Optional<TrainSchedule> trainSchedule = trainScheduleService.getTrainById(trainId);
        return trainSchedule.map(schedule -> new ResponseEntity<>(schedule, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public TrainSchedule createTrainSchedule(@RequestBody TrainSchedule trainSchedule) {
        return trainScheduleService.createTrainSchedule(trainSchedule);
    }
}