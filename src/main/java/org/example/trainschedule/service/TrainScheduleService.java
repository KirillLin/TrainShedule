package org.example.trainschedule.service;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.domain.TrainSchedule;
import org.example.trainschedule.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainScheduleService {
    private final TrainScheduleRepository trainScheduleRepository;

    @Autowired
    public TrainScheduleService(TrainScheduleRepository trainScheduleRepository) {
        this.trainScheduleRepository = trainScheduleRepository;
    }

    @SuppressWarnings("checkstyle:LineLength")
    public List<TrainSchedule> searchTrains(String departureStation, String arrivalStation) {
        if (departureStation != null && arrivalStation != null) {
            return trainScheduleRepository.findByDepartureStationIgnoreCaseAndArrivalStationIgnoreCase(departureStation, arrivalStation);
        } else {
            return trainScheduleRepository.findAll();
        }
    }

    public Optional<TrainSchedule> getTrainById(String trainId) {
        return trainScheduleRepository.findById(trainId);
    }

    public TrainSchedule createTrainSchedule(TrainSchedule trainSchedule) {
        return trainScheduleRepository.save(trainSchedule);
    }
}