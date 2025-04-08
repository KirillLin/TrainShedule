package org.example.trainschedule.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.mappers.SeatMapper;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.example.trainschedule.cache.TrainCache;
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
    private final TrainCache trainCache;

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

    @SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:LineLength"})
    @Transactional
    public SeatDTO updateSeat(Long id, SeatDTO seatDTO) {
        Seat existingSeat = seatRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + id));

        Train train = null;
        if (seatDTO.getTrainId() != null) {
            train = trainRepository.findById(seatDTO.getTrainId())
                    .orElseThrow(() -> new EntityNotFoundException("Train not found with id: " + seatDTO.getTrainId()));

            if (!existingSeat.getNumber().equals(seatDTO.getNumber()) &&
                    seatRepository.existsByTrainIdAndNumber(seatDTO.getTrainId(), seatDTO.getNumber())) {
                throw new IllegalStateException("Seat with number " + seatDTO.getNumber() + " already exists in this train");
            }
        }

        seatMapper.updateEntityFromDto(seatDTO, existingSeat, train);
        Seat updatedSeat = seatRepository.save(existingSeat);
        trainCache.clearAll();
        return seatMapper.toDto(updatedSeat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new EntityNotFoundException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
        trainCache.clearAll();
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
                .isFree(seatDTO.getIsFree())
                .type(seatDTO.getType())
                .price(seatDTO.getPrice())
                .build();

        Seat savedSeat = seatRepository.save(seat);
        trainCache.clearAll();
        return seatMapper.toDto(savedSeat);
    }
}

