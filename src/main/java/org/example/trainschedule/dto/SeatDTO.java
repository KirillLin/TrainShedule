package org.example.trainschedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
@Schema(description = "Seat information DTO")
public class SeatDTO {
    @Schema(hidden = true)
    private Long id;

    @Schema(hidden = true)
    private Long trainId;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be ≥ 1")
    @Digits(integer = 10, fraction = 0, message = "Must be an integer")
    @Schema(description = "Seat number (must be positive integer)", example = "15")
    private Integer number;

    @NotNull(message = "Seat status is required")
    @Schema(description = "Availability status of the seat", example = "true/false or 0/1")
    private Boolean isFree;

    @NotBlank(message = "Seat type is required")
    @Schema(description = "Type/class of the seat", example = "Business")
    private String type;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Schema(description = "Price for this seat", example = "1250.50")
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