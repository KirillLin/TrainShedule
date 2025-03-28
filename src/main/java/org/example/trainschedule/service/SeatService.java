package org.example.trainschedule.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.mappers.SeatMapper;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.example.trainschedule.repository.SeatRepository;
import org.example.trainschedule.repository.TrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final SeatRepository seatRepository;
    private final TrainRepository trainRepository;
    private final SeatMapper seatMapper;

    @Transactional(readOnly = true)
    public List<SeatDTO> getAllSeats() {
        return seatRepository.findAll().stream()
                .map(seatMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public SeatDTO getSeatById(Long id) {
        return seatRepository.findById(id)
                .map(seatMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + id));
    }

    @Transactional(readOnly = true)
        public List<SeatDTO> getSeatsByTrainId(Long trainId) {
        return seatRepository.findByTrainId(trainId).stream()
                .map(seatMapper::toDto)
                .toList();
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Transactional
    public SeatDTO createSeat(SeatDTO seatDTO) {
        Train train = trainRepository.findById(seatDTO.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train not found with id: " + seatDTO.getTrainId()));

        if (seatRepository.existsByTrainIdAndNumber(seatDTO.getTrainId(), seatDTO.getNumber())) {
            throw new IllegalStateException("Seat with number " + seatDTO.getNumber() + " already exists in this train");
        }

        Seat seat = seatMapper.toEntity(seatDTO, train);
        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDto(savedSeat);
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Transactional
    public SeatDTO updateSeat(Long id, SeatDTO seatDTO) {
        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + id));

        Train train = trainRepository.findById(seatDTO.getTrainId())
                .orElseThrow(() -> new EntityNotFoundException("Train not found with id: " + seatDTO.getTrainId()));

        if (!existingSeat.getNumber().equals(seatDTO.getNumber()) &&
                    seatRepository.existsByTrainIdAndNumber(seatDTO.getTrainId(), seatDTO.getNumber())) {
            throw new IllegalStateException("Seat with number " + seatDTO.getNumber() + " already exists in this train");
        }

        existingSeat.setTrain(train);
        existingSeat.setNumber(seatDTO.getNumber());
        existingSeat.setType(seatDTO.getType());
        existingSeat.setPrice(seatDTO.getPrice());

        Seat updatedSeat = seatRepository.save(existingSeat);
        return seatMapper.toDto(updatedSeat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new EntityNotFoundException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    public SeatDTO addSeatToTrain(Long trainId, SeatDTO seatDTO) {
        if (!trainId.equals(seatDTO.getTrainId())) {
            throw new IllegalArgumentException("Train ID in path and body must match");
        }

        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new EntityNotFoundException("Train not found"));

        Seat seat = Seat.builder()
                .train(train)
                .number(seatDTO.getNumber())
                .type(seatDTO.getType())
                .price(seatDTO.getPrice())
                .build();

        Seat savedSeat = seatRepository.save(seat);
        return seatMapper.toDto(savedSeat);
    }

    @Transactional
    public SeatDTO unbindSeat(Long seatId, Long newTrainId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found"));

        if (newTrainId != null) {
            Train newTrain = trainRepository.findById(newTrainId)
                    .orElseThrow(() -> new EntityNotFoundException("New train not found"));
            seat.setTrain(newTrain);
        } else {
            seat.setTrain(null);
        }

        Seat updatedSeat = seatRepository.save(seat);
        return seatMapper.toDto(updatedSeat);
    }
}

