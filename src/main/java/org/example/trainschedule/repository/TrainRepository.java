package org.example.trainschedule.repository;

import java.util.List;
import java.util.Optional;
import org.example.trainschedule.model.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByNumber(String number);

    boolean existsByNumber(String number);

    List<Train> findByNumberContainingIgnoreCase(String number);
}

