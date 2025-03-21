package org.example.trainschedule.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("checkstyle:LineLength")
@Entity
@Getter
@Setter
@NoArgsConstructor

public class TrainSchedule {
    @Id
    private String trainNumber;

    private String departureStation;

    private String arrivalStation;

    private String departureTime;

    private String arrivalTime;

    @OneToMany(mappedBy = "trainSchedule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Passenger> passengers;
}
