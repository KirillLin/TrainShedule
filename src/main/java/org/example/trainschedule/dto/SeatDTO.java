package org.example.trainschedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.trainschedule.model.Seat;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
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
        if (seat == null) {
            return null;
        }

        return SeatDTO.builder()
                .id(seat.getId())
                .trainId(seat.getTrain() != null ? seat.getTrain().getId() : null)
                .number(seat.getNumber())
                .type(seat.getType())
                .price(seat.getPrice())
                .build();
    }
}