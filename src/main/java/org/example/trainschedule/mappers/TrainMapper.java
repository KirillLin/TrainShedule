package org.example.trainschedule.mappers;

import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
                .seats(train.getSeats().stream()
                        .map(seatMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

    public Train toEntity(TrainDTO trainDTO) {
        return Train.builder()
                .id(trainDTO.getId())
                .number(trainDTO.getNumber())
                .build();
    }

    public void updateEntityFromDto(TrainDTO trainDTO, Train train) {
        if (trainDTO.getNumber() != null) {
            train.setNumber(trainDTO.getNumber());
        }

        if (trainDTO.getSeats() != null && !trainDTO.getSeats().isEmpty()) {
            train.getSeats().clear();
            trainDTO.getSeats().forEach(seatDTO -> {
                Seat seat = seatMapper.toEntity(seatDTO, train);
                train.getSeats().add(seat);
            });
        }
    }
}