package org.example.trainschedule.controller;

import java.util.List;
import org.example.trainschedule.model.Passenger;
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

    @GetMapping("/{trainNumber}")
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
}