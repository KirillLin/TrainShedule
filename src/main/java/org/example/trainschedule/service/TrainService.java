package org.example.trainschedule.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.mappers.SeatMapper;
import org.example.trainschedule.mappers.TrainMapper;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.example.trainschedule.repository.SeatRepository;
import org.example.trainschedule.repository.TrainRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainService {
    private final TrainRepository trainRepository;
    private final SeatRepository seatRepository;
    private final TrainMapper trainMapper;
    private final SeatMapper seatMapper;

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Transactional
    public SeatDTO addSeatToTrain(Long trainId, SeatDTO seatDTO) {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found"));

        Seat seat = Seat.builder()
                .number(seatDTO.getNumber())
                .type(seatDTO.getType())
                .price(seatDTO.getPrice())
                .build();

        train.addSeat(seat);

        trainRepository.save(train);

        return convertToSeatDTO(seat);
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    private SeatDTO convertToSeatDTO(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .trainId(seat.getTrain() != null ? seat.getTrain().getId() : null)
                .number(seat.getNumber())
                .type(seat.getType())
                .price(seat.getPrice())
                .build();
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public TrainDTO createTrain(TrainDTO trainDTO) {
        if (trainRepository.existsByNumber(trainDTO.getNumber())) {
            throw new IllegalStateException("Train with number " + trainDTO.getNumber() + " already exists");
        }

        Train train = trainMapper.toEntity(trainDTO);
        Train savedTrain = trainRepository.save(train);
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

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
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
        return trainMapper.toDto(updatedTrain);
    }

    public void deleteTrain(Long id) {
        if (!trainRepository.existsById(id)) {
            throw new EntityNotFoundException("Train not found with id: " + id);
        }
        trainRepository.deleteById(id);
    }
}