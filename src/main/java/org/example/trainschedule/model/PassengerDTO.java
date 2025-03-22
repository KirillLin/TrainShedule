package org.example.trainschedule.model;

import lombok.Data;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Data
public class PassengerDTO {
    private Long passengerId;
    private String name;
    private TrainScheduleShortDTO trainSchedule;
}