package org.example.trainschedule.mappers;

import org.springframework.stereotype.Component;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;

@Component
public class SeatMapper {
    public SeatDTO toDto(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .trainId(seat.getTrain() != null ? seat.getTrain().getId() : null)
                .number(seat.getNumber())
                .type(seat.getType())
                .price(seat.getPrice())
                .build();
    }

    public Seat toEntity(SeatDTO seatDTO, Train train) {
        Seat seat = new Seat();
        seat.setId(seatDTO.getId());
        seat.setTrain(train);
        seat.setNumber(seatDTO.getNumber());
        seat.setType(seatDTO.getType());
        seat.setPrice(seatDTO.getPrice());
        return seat;
    }
}
