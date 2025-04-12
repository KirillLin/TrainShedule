package org.example.trainschedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.trainschedule.model.Train;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Train number is required")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Train number must contain only letters and digits")
    private String number;

    @NotBlank(message = "Departure station is required")
    private String departureStation;

    @NotBlank(message = "Arrival station is required")
    private String arrivalStation;

    @NotBlank(message = "Departure time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format, expected HH:MM")
    private String departureTime;

    @NotBlank(message = "Arrival time is required")
    @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format, expected HH:MM")
    private String arrivalTime;

    private List<SeatDTO> seats;

    public static TrainDTO fromEntity(Train train) {
        if (train == null) {
            return null;
        }

        return TrainDTO.builder()
                .id(train.getId())
                .number(train.getNumber())
                .departureStation(train.getDepartureStation())
                .arrivalStation(train.getArrivalStation())
                .departureTime(train.getDepartureTime())
                .arrivalTime(train.getArrivalTime())
                .seats(train.getSeats() != null ?
                        train.getSeats().stream()
                                .map(SeatDTO::fromEntity)
                                .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }
}