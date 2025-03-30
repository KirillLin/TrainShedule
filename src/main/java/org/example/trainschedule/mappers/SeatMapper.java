package org.example.trainschedule.mappers;

import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.springframework.stereotype.Component;

@Component
public class SeatMapper {
    public SeatDTO toDto(Seat seat) {
        if (seat == null) return null;

        return SeatDTO.builder()
                .id(seat.getId())
                .trainId(seat.getTrain() != null ? seat.getTrain().getId() : null)
                .number(seat.getNumber())
                .isFree(seat.getIsFree())
                .type(seat.getType())
                .price(seat.getPrice())
                .build();
    }

    public void updateEntityFromDto(SeatDTO seatDTO, Seat seat, Train train) {
        if (seatDTO.getNumber() != null) {
            seat.setNumber(seatDTO.getNumber());
        }
        if (seatDTO.getIsFree() != null) {
            seat.setIsFree(seatDTO.getIsFree());
        }
        if (seatDTO.getType() != null) {
            seat.setType(seatDTO.getType());
        }
        if (seatDTO.getPrice() != null) {
            seat.setPrice(seatDTO.getPrice());
        }
        if (train != null) {
            seat.setTrain(train);
        }
    }
}
