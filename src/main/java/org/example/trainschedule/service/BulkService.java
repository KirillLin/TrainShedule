package org.example.trainschedule.service;

import java.util.*;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.exceptions.GlobalExceptionHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trainschedule.repository.TrainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BulkService {
    private final TrainService trainService;
    private final TrainRepository trainRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(BulkService.class);

    public BulkService(TrainService trainService,
                       TrainRepository trainRepository) {
        this.trainService = trainService;
        this.trainRepository = trainRepository;
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Transactional
    public List<TrainDTO> createTrains(List<TrainDTO> trainDTOs) {
        if (trainDTOs == null || trainDTOs.isEmpty()) {
            return Collections.emptyList();
        }
        Set<String> existingNumbers = trainRepository.findAllNumbers();

        List<TrainDTO> createdTrains = new ArrayList<>();

        for (TrainDTO trainDTO : trainDTOs) {
            if (!existingNumbers.contains(trainDTO.getNumber())) {
                try {
                    TrainDTO createdTrain = trainService.createTrain(trainDTO);
                    createdTrains.add(createdTrain);
                    existingNumbers.add(trainDTO.getNumber());
                } catch (Exception e) {
                    LOGGER.info("Поезд уже существует: {}", trainDTO.getNumber(), e);
                }
            }
        }

        return createdTrains;
    }
}
