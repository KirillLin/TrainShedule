package org.example.trainschedule.repository;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTrainId(Long trainId);

    List<Seat> findByTrainIdAndTypeIgnoreCase(Long trainId, String type);

    boolean existsByTrainIdAndNumber(Long trainId, Integer number);

    Optional<Seat> findByTrainAndNumber(Train train, Integer number);
}