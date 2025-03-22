package org.example.trainschedule.repository;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findById(Long passengerId);
    List<Passenger> findByTrainSchedule_TrainNumber(String trainNumber);
}
