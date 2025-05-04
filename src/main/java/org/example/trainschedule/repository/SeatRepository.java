package org.example.trainschedule.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByTrainId(Long trainId);

    List<Seat> findByTrainIdAndTypeIgnoreCase(Long trainId, String type);

    boolean existsByTrainIdAndNumber(Long trainId, Integer number);

    Optional<Seat> findByTrainAndNumber(Train train, Integer number);

    List<Seat> findByTrainIdOrderByNumberAsc(Long trainId);

    @Query("SELECT COUNT(s) FROM Seat s WHERE s.id IN :ids")
    long countByIdIn(@Param("ids") Set<Long> ids);

    @Query("SELECT s.number FROM Seat s WHERE s.train.id = :trainId")
    List<Integer> findNumbersByTrainId(@Param("trainId") Long trainId);
}