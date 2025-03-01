package org.example.trainschedule.service;

import org.example.trainschedule.domain.TrainSchedule;
import org.example.trainschedule.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainScheduleService {

    private final TrainScheduleRepository trainScheduleRepository;

    @Autowired
    public TrainScheduleService(TrainScheduleRepository trainScheduleRepository) {
        this.trainScheduleRepository = trainScheduleRepository;
    }

    public List<TrainSchedule> searchTrains(String departureStation, String arrivalStation) {
        if (departureStation != null && arrivalStation != null) {
            return trainScheduleRepository.findByDepartureStationIgnoreCaseAndArrivalStationIgnoreCase(departureStation, arrivalStation);
        } else {
            return trainScheduleRepository.findAll();
        }
    }

    public TrainSchedule getTrainById(String trainId) {
        return trainScheduleRepository.findById(trainId).orElse(null);
    }

    public TrainSchedule createTrainSchedule(TrainSchedule trainSchedule) {
        return trainScheduleRepository.save(trainSchedule);
    }
    public TrainSchedule updateTrainSchedule(String trainId, TrainSchedule trainSchedule) {
        TrainSchedule existingSchedule = trainScheduleRepository.findById(trainId).orElse(null);
        if (existingSchedule != null) {
            trainSchedule.setTrainId(existingSchedule.getTrainId()); // Keep the ID
            return trainScheduleRepository.save(trainSchedule);
        }
        return null; // Or throw an exception
    }

    public void deleteTrainSchedule(String trainId) {
        trainScheduleRepository.deleteById(trainId);
    }
}