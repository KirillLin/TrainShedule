package org.example.trainschedule.mappers;

import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {
    public SeatDTO toDto(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .trainId(seat.getTrain().getId())
                .number(seat.getNumber())
                .type(seat.getType())
                .price(seat.getPrice())
                .build();
    }

    public Seat toEntity(SeatDTO seatDTO, Train train) {
        return Seat.builder()
                .id(seatDTO.getId())
                .train(train)
                .number(seatDTO.getNumber())
                .type(seatDTO.getType())
                .price(seatDTO.getPrice())
                .build();
    }
}
