package org.example.trainschedule;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("checkstyle:RegexpMultiline")
@RestController
@RequestMapping("/trains")
public class TrainScheduleController {

    private List<TrainSchedule> trainSchedules = new ArrayList<>(); // In-memory storage (for demo)

    public TrainScheduleController() {
        trainSchedules.add(new TrainSchedule("123A", "Moscow", "Saint Petersburg", "08:00", "12:00"));
        trainSchedules.add(new TrainSchedule("456B", "Saint Petersburg", "Moscow", "14:00", "18:00"));
    }

    @SuppressWarnings({"checkstyle:RegexpMultiline", "checkstyle:LineLength"})
    @GetMapping("/search")
    public List<TrainSchedule> searchTrains(
            @RequestParam(value = "departure", required = false) String departureStation,
            @RequestParam(value = "arrival", required = false) String arrivalStation) {

        List<TrainSchedule> results = new ArrayList<>();
        for (TrainSchedule schedule : trainSchedules) {
            if ((departureStation == null || schedule.getDepartureStation().equalsIgnoreCase(departureStation)) &&
                    (arrivalStation == null || schedule.getArrivalStation().equalsIgnoreCase(arrivalStation))) {
                results.add(schedule);
            }
        }
        return results;
    }

    @GetMapping("/{trainId}")
    public TrainSchedule getTrainById(@PathVariable String trainId) {
        for (TrainSchedule schedule : trainSchedules) {
            if (schedule.getTrainId().equals(trainId)) {
                return schedule;
            }
        }
        return null;
    }
}

