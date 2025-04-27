package org.example.trainschedule.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.repository.TrainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        return trainDTOs.stream()
                .filter(trainDTO -> !existingNumbers.contains(trainDTO.getNumber()))
                .map(trainDTO -> {
                    try {
                        TrainDTO createdTrain = trainService.createTrain(trainDTO);
                        existingNumbers.add(trainDTO.getNumber());
                        return createdTrain;
                    } catch (Exception e) {
                        LOGGER.info("Поезд уже существует: {}", trainDTO.getNumber(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
