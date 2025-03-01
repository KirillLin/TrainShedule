package org.example.trainschedule.controller;

import org.example.trainschedule.domain.TrainSchedule;
import org.example.trainschedule.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trains")
public class TrainScheduleController {

    private final TrainScheduleService trainScheduleService;

    @Autowired
    public TrainScheduleController(TrainScheduleService trainScheduleService) {
        this.trainScheduleService = trainScheduleService;
    }

    @GetMapping("/search")
    public List<TrainSchedule> searchTrains(
            @RequestParam(value = "departure", required = false) String departureStation,
            @RequestParam(value = "arrival", required = false) String arrivalStation) {
        return trainScheduleService.searchTrains(departureStation, arrivalStation);
    }

    @GetMapping("/{trainId}")
    public TrainSchedule getTrainById(@PathVariable String trainId) {
        return trainScheduleService.getTrainById(trainId);
    }

    @PostMapping
    public TrainSchedule createTrainSchedule(@RequestBody TrainSchedule trainSchedule) {
        return trainScheduleService.createTrainSchedule(trainSchedule);
    }

    @PutMapping("/{trainId}")
    public TrainSchedule updateTrainSchedule(@PathVariable String trainId, @RequestBody TrainSchedule trainSchedule) {
        return trainScheduleService.updateTrainSchedule(trainId, trainSchedule);
    }

    @DeleteMapping("/{trainId}")
    public void deleteTrainSchedule(@PathVariable String trainId) {
        trainScheduleService.deleteTrainSchedule(trainId);
    }
}
