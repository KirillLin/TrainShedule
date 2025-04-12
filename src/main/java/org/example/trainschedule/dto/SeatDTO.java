package org.example.trainschedule.dto;

import jakarta.validation.constraints.*;
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

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be ≥ 1")
    @Digits(integer = 10, fraction = 0, message = "Must be an integer")
    private Integer number;

    @NotNull(message = "Seat status is required")
    private Boolean isFree;

    @NotBlank(message = "Seat type is required")
    private String type;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    private Double price;

    public static SeatDTO fromEntity(Seat seat) {
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
}