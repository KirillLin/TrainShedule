package org.example.trainschedule.model;

import lombok.Data;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Data
public class TrainScheduleShortDTO {
    private String trainNumber;
    private String departureStation;
    private String arrivalStation;
}
