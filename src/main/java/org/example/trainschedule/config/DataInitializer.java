package org.example.trainschedule.config;

import org.example.trainschedule.domain.TrainSchedule;
import org.example.trainschedule.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@SuppressWarnings("checkstyle:RegexpMultiline")
@Component
public class DataInitializer implements CommandLineRunner {

    private final TrainScheduleService trainScheduleService;

    @Autowired
    public DataInitializer(TrainScheduleService trainScheduleService) {
        this.trainScheduleService = trainScheduleService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create some TrainSchedule objects
        TrainSchedule schedule1 = new TrainSchedule("123A", "Minsk", "Saint Petersburg", "10:00", "18:00");
        TrainSchedule schedule2 = new TrainSchedule("101D", "Saint Petersburg", "Polotsk", "14:00", "06:00");

        // Save them using TrainScheduleService
        trainScheduleService.createTrainSchedule(schedule1);
        trainScheduleService.createTrainSchedule(schedule2);

        System.out.println("Train schedules initialized and saved to the database.");
    }
}