package org.example.trainschedule.dto;

import lombok.*;
import org.example.trainschedule.model.Seat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {
    private Long id;
    private Long trainId;
    private Integer number;
    private String type;
    private double price;

    public static SeatDTO fromEntity(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .trainId(seat.getTrain().getId())
                .number(seat.getNumber())
                .type(seat.getType())
                .price(seat.getPrice())
                .build();
    }
}