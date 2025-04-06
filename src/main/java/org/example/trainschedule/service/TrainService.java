package org.example.trainschedule.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.mappers.SeatMapper;
import org.example.trainschedule.mappers.TrainMapper;
import org.example.trainschedule.model.Train;
import org.example.trainschedule.model.TrainCache;
import org.example.trainschedule.repository.SeatRepository;
import org.example.trainschedule.repository.TrainRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainService {
    private final TrainRepository trainRepository;
    private final SeatRepository seatRepository;
    private final TrainMapper trainMapper;
    private final SeatMapper seatMapper;
    private final TrainCache trainCache;
    @SuppressWarnings("checkstyle:ConstantName")
    private static final Logger logger = LoggerFactory.getLogger(TrainService.class);

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public TrainDTO createTrain(TrainDTO trainDTO) {
        if (trainRepository.existsByNumber(trainDTO.getNumber())) {
            throw new IllegalStateException("Train with number " + trainDTO.getNumber() + " already exists");
        }

        Train train = trainMapper.toEntity(trainDTO);
        Train savedTrain = trainRepository.save(train);
        trainCache.clearAll();
        return trainMapper.toDto(savedTrain);
    }

    public TrainDTO getTrainById(Long id) {
        return trainRepository.findById(id)
                .map(trainMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with id: " + id));
    }

    public List<TrainDTO> getAllTrains() {
        return trainRepository.findAll().stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TrainDTO> searchByNumber(String number) {
        return trainRepository.findByNumberContainingIgnoreCase(number).stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:LineLength"})
    @Transactional
    public TrainDTO updateTrainByNumber(String trainNumber, TrainDTO trainDTO) {
        Train train = trainRepository.findByNumber(trainNumber)
                .orElseThrow(() -> new EntityNotFoundException("Train not found with number: " + trainNumber));

        if (trainDTO.getDepartureStation() != null) {
            train.setDepartureStation(trainDTO.getDepartureStation());
        }
        if (trainDTO.getArrivalStation() != null) {
            train.setArrivalStation(trainDTO.getArrivalStation());
        }
        if (trainDTO.getDepartureTime() != null) {
            train.setDepartureTime(trainDTO.getDepartureTime());
        }
        if (trainDTO.getArrivalTime() != null) {
            train.setArrivalTime(trainDTO.getArrivalTime());
        }

        Train updatedTrain = trainRepository.save(train);
        trainCache.clearAll();
        return trainMapper.toDto(updatedTrain);
    }

    public void deleteTrain(Long id) {
        if (!trainRepository.existsById(id)) {
            throw new EntityNotFoundException("Train not found with id: " + id);
        }
        trainRepository.deleteById(id);
        trainCache.clearAll();
    }

    @Transactional(readOnly = true)
    public List<TrainDTO> findTrainsWithFreeSeats(String departure, String arrival) {
        String cacheKey = "trains:" + departure + ":" + arrival;

        List<TrainDTO> cached = trainCache.getWithMetrics(cacheKey);
        if (cached != null) {
            return cached;
        }

        logger.info("Запрос к БД для маршрута: {} -> {}", departure, arrival);
        long dbQueryStart = System.currentTimeMillis();

        List<Train> trains = trainRepository.findTrainsWithFreeSeats(departure, arrival);
        List<TrainDTO> result = trains.stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());

        long dbQueryTime = System.currentTimeMillis() - dbQueryStart;
        logger.info("Запрос к БД выполнен за {} мс | Найдено поездов: {}",
                dbQueryTime, result.size());

        trainCache.put(cacheKey, result);
        return result;
    }
}