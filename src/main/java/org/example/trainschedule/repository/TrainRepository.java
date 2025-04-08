package org.example.trainschedule.repository;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByNumber(String number);

    boolean existsByNumber(String number);

    List<Train> findByNumberContainingIgnoreCase(String number);

    @Query("SELECT DISTINCT t FROM Train t " +
            "JOIN t.seats s " +
            "WHERE t.departureStation = :departure " +
            "AND t.arrivalStation = :arrival " +
            "AND s.isFree = true")
    List<Train> findTrainsWithFreeSeats(
            @Param("departure") String departure,
            @Param("arrival") String arrival);

    @Query(value = "SELECT DISTINCT t.* FROM train t " +
            "JOIN seats s ON t.id = s.train_id " +
            "WHERE t.departure_station = :departure " +
            "AND t.arrival_station = :arrival " +
            "AND s.is_free = true",
            nativeQuery = true)
    List<Train> findTrainsWithFreeSeatsNative(
            @Param("departure") String departure,
            @Param("arrival") String arrival);
}


