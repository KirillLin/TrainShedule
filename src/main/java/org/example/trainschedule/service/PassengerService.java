package org.example.trainschedule.service;

import java.util.List;
import org.example.trainschedule.model.Passenger;
import org.example.trainschedule.model.TrainSchedule;
import org.example.trainschedule.repository.PassengerRepository;
import org.example.trainschedule.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassengerService {
    private final PassengerRepository passengerRepository;
    private final TrainScheduleRepository trainScheduleRepository;

    @SuppressWarnings("checkstyle:LineLength")
    @Autowired
    public PassengerService(PassengerRepository passengerRepository, TrainScheduleRepository trainScheduleRepository) {
        this.passengerRepository = passengerRepository;
        this.trainScheduleRepository = trainScheduleRepository;
    }

    @SuppressWarnings("checkstyle:LineLength")
    @Transactional
    public Passenger addPassengerToTrain(String trainNumber, Passenger passenger) {
        TrainSchedule trainSchedule = trainScheduleRepository.findById(trainNumber)
                .orElseThrow(() -> new IllegalArgumentException("Train not found with number: " + trainNumber));

        passenger.setTrainSchedule(trainSchedule);
        return passengerRepository.save(passenger);
    }

    @SuppressWarnings("checkstyle:LineLength")
    public List<Passenger> getPassengersByTrainNumber(String trainNumber) {
        TrainSchedule trainSchedule = trainScheduleRepository.findById(trainNumber)
                .orElseThrow(() -> new IllegalArgumentException("Train not found with number: " + trainNumber));
        return trainSchedule.getPassengers();
    }

    public Passenger createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }
}