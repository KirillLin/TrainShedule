package org.example.trainschedule.repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.example.trainschedule.model.Train;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {
    List<Train> findByNumberContainingIgnoreCase(String number);
}
