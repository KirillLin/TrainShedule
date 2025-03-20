package org.example.trainschedule.repository;

import java.util.List;
import org.example.trainschedule.model.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings({"checkstyle:RegexpMultiline", "checkstyle:LineLength"})
@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, String> {

    List<TrainSchedule> findByDepartureStationIgnoreCaseAndArrivalStationIgnoreCase(String departureStation, String arrivalStation);
}
