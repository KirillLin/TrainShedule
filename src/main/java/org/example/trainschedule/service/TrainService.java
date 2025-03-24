package org.example.trainschedule.service;

import jakarta.persistence.EntityNotFoundException;
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
public class TrainService {
    private final TrainRepository trainRepository;
    private final SeatRepository seatRepository;
    private final TrainMapper trainMapper;
    private final SeatMapper seatMapper;

    public List<TrainDTO> getAllTrains() {
        return trainRepository.findAll().stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    public TrainDTO getTrainById(Long id) {
        return trainRepository.findById(id)
                .map(trainMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Train not found"));
    }

    public List<TrainDTO> searchByNumber(String number) {
        return trainRepository.findByNumberContainingIgnoreCase(number).stream()
                .map(trainMapper::toDto)
                .collect(Collectors.toList());
    }

    public TrainDTO createTrain(TrainDTO trainDTO) {
        Train train = trainMapper.toEntity(trainDTO);
        Train savedTrain = trainRepository.save(train);
        return trainMapper.toDto(savedTrain);
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    public List<SeatDTO> getTrainSeats(Long trainId) {
        return seatRepository.findByTrainId(trainId).stream()
                .map(seatMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SeatDTO> getTrainSeatsByType(Long trainId, String type) {
        return seatRepository.findByTrainIdAndTypeIgnoreCase(trainId, type).stream()
                .map(seatMapper::toDto)
                .collect(Collectors.toList());
    }

    public SeatDTO addSeatToTrain(Long trainId, SeatDTO seatDTO) {
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found"));

        Seat seat = seatMapper.toEntity(seatDTO, train);
        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDto(savedSeat);
    }

    public void deleteSeat(Long trainId, Long seatId) {
        seatRepository.deleteByIdAndTrainId(seatId, trainId);
    }
}