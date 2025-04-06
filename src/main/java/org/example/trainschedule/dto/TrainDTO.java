package org.example.trainschedule.dto;

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
    private String number;
    private String departureStation;
    private String arrivalStation;
    private String departureTime;
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