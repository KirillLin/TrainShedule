package org.example.trainschedule.repository;

import org.example.trainschedule.domain.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, String> {

    List<TrainSchedule> findByDepartureStationIgnoreCaseAndArrivalStationIgnoreCase(String departureStation, String arrivalStation);
}