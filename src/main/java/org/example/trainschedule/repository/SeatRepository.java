package org.example.trainschedule.repository;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTrainId(Long trainId);
    List<Seat> findByTrainIdAndTypeIgnoreCase(Long trainId, String type);
    void deleteByIdAndTrainId(Long seatId, Long trainId);
}