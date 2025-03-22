package org.example.trainschedule.controller;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.Passenger;
import org.example.trainschedule.model.PassengerDTO;
import org.example.trainschedule.model.TrainScheduleShortDTO;
import org.example.trainschedule.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/passengers")
public class PassengerController {
    private final PassengerService passengerService;

    @Autowired
    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @PostMapping("/{trainNumber}")
    public ResponseEntity<Passenger> addPassengerToTrain(
            @PathVariable String trainNumber,
            @RequestBody Passenger passenger) {
        try {
            Passenger addedPassenger = passengerService.addPassengerToTrain(trainNumber, passenger);
            return new ResponseEntity<>(addedPassenger, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @GetMapping("/id/{passengerId}")
    public ResponseEntity<?> getPassengerById(@PathVariable Long passengerId) {
        Optional<Passenger> passengerOptional = passengerService.getPassengerById(passengerId);

        if (passengerOptional.isPresent()) {
            Passenger passenger = passengerOptional.get();
            PassengerDTO passengerDTO = convertToDTO(passenger);
            return new ResponseEntity<>(passengerDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Passenger not found", HttpStatus.NOT_FOUND);
        }
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private PassengerDTO convertToDTO(Passenger passenger) {
        PassengerDTO dto = new PassengerDTO();
        dto.setPassengerId(passenger.getPassengerId());
        dto.setName(passenger.getName());

        if (passenger.getTrainSchedule() != null) {
            TrainScheduleShortDTO trainDTO = new TrainScheduleShortDTO();
            trainDTO.setTrainNumber(passenger.getTrainSchedule().getTrainNumber());
            trainDTO.setDepartureStation(passenger.getTrainSchedule().getDepartureStation());
            trainDTO.setArrivalStation(passenger.getTrainSchedule().getArrivalStation());
            dto.setTrainSchedule(trainDTO);
        }

        return dto;
    }

    @GetMapping("/train/{trainNumber}")
    public ResponseEntity<List<Passenger>> getPassengersByTrainNumber(@PathVariable String trainNumber) {
        try {
            List<Passenger> passengers = passengerService.getPassengersByTrainNumber(trainNumber);
            return new ResponseEntity<>(passengers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Passenger> createPassenger(@RequestBody Passenger passenger) {
        Passenger createdPassenger = passengerService.createPassenger(passenger);
        return new ResponseEntity<>(createdPassenger, HttpStatus.CREATED);
    }

    @DeleteMapping("/{passengerId}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long passengerId) {
        passengerService.deletePassenger(passengerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{passengerId}/removeTrain")
    public ResponseEntity<Void> removePassengerFromTrain(@PathVariable Long passengerId) {
        try {
            passengerService.removePassengerFromTrain(passengerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}