package org.example.trainschedule.mappers;

import java.util.stream.Collectors;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.model.Train;
import org.springframework.stereotype.Component;

@Component
public class TrainMapper {
    private final SeatMapper seatMapper;

    public TrainMapper(SeatMapper seatMapper) {
        this.seatMapper = seatMapper;
    }

    public TrainDTO toDto(Train train) {
        return TrainDTO.builder()
                .id(train.getId())
                .number(train.getNumber())
                .departureStation(train.getDepartureStation())
                .arrivalStation(train.getArrivalStation())
                .departureTime(train.getDepartureTime())
                .arrivalTime(train.getArrivalTime())
                .seats(train.getSeats().stream()
                        .map(seatMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public Train toEntity(TrainDTO trainDTO) {
        return Train.builder()
                .id(trainDTO.getId())
                .number(trainDTO.getNumber())
                .departureStation(trainDTO.getDepartureStation())
                .arrivalStation(trainDTO.getArrivalStation())
                .departureTime(trainDTO.getDepartureTime())
                .arrivalTime(trainDTO.getArrivalTime())
                .build();
    }
}