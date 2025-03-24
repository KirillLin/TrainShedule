package org.example.trainschedule.dto;

import lombok.*;
import org.example.trainschedule.model.Train;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainDTO {
    private Long id;
    private String number;
    private List<SeatDTO> seats;

    public static TrainDTO fromEntity(Train train) {
        return TrainDTO.builder()
                .id(train.getId())
                .number(train.getNumber())
                .seats(train.getSeats().stream()
                        .map(SeatDTO::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
