package org.example.trainschedule.service;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.TrainSchedule;
import org.example.trainschedule.repository.PassengerRepository;
import org.example.trainschedule.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainScheduleService {
    private final TrainScheduleRepository trainScheduleRepository;
    private final PassengerRepository passengerRepository;

    @SuppressWarnings("checkstyle:LineLength")
    @Autowired
    public TrainScheduleService(TrainScheduleRepository trainScheduleRepository, PassengerRepository passengerRepository) {
        this.trainScheduleRepository = trainScheduleRepository;
        this.passengerRepository = passengerRepository;
    }

    @SuppressWarnings("checkstyle:LineLength")
    public List<TrainSchedule> searchTrains(String departureStation, String arrivalStation) {
        if (departureStation != null && arrivalStation != null) {
            return trainScheduleRepository.findByDepartureStationIgnoreCaseAndArrivalStationIgnoreCase(departureStation, arrivalStation);
        } else {
            return trainScheduleRepository.findAll();
        }
    }

    public Optional<TrainSchedule> getTrainByNumber(String trainNumber) {
        return trainScheduleRepository.findById(trainNumber);
    }

    public TrainSchedule createTrainSchedule(TrainSchedule trainSchedule) {
        return trainScheduleRepository.save(trainSchedule);
    }

    @SuppressWarnings("checkstyle:LineLength")
    @Transactional
    public TrainSchedule updateTrainSchedule(String trainNumber, TrainSchedule trainSchedule) {
        TrainSchedule existingTrain = trainScheduleRepository.findById(trainNumber)
                .orElseThrow(() -> new IllegalArgumentException("Train not found with number: " + trainNumber));

        existingTrain.setDepartureStation(trainSchedule.getDepartureStation());
        existingTrain.setArrivalStation(trainSchedule.getArrivalStation());
        existingTrain.setDepartureTime(trainSchedule.getDepartureTime());
        existingTrain.setArrivalTime(trainSchedule.getArrivalTime());

        return trainScheduleRepository.save(existingTrain);
    }

    public void deleteTrainSchedule(String trainNumber) {
        trainScheduleRepository.deleteById(trainNumber);
    }
}