package org.example.trainschedule.Service;

import org.example.trainschedule.cache.TrainCache;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.mappers.TrainMapper;
import org.example.trainschedule.model.Train;
import org.example.trainschedule.repository.TrainRepository;
import org.example.trainschedule.service.TrainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainServiceTest {

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private TrainCache trainCache;

    @Mock
    private TrainMapper trainMapper;

    @InjectMocks
    private TrainService trainService;

    private TrainDTO baseTrainDTO;
    private Train baseTrain;
    private SeatDTO baseSeatDTO;

    @BeforeEach
    void setUp() {
        baseSeatDTO = SeatDTO.builder()
                .id(1L)
                .trainId(1L)
                .number(1)
                .type("Business")
                .price(100.0)
                .isFree(true)
                .build();

        baseTrainDTO = TrainDTO.builder()
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .seats(Collections.singletonList(baseSeatDTO))
                .build();

        baseTrain = Train.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();
    }

    @Test
    void createTrain_Success() {
        TrainDTO inputDTO = TrainDTO.builder()
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        when(trainRepository.existsByNumber("123")).thenReturn(false);
        when(trainMapper.toEntity(inputDTO)).thenReturn(baseTrain);
        when(trainRepository.save(baseTrain)).thenReturn(baseTrain);
        when(trainMapper.toDto(baseTrain)).thenReturn(expectedDTO);

        TrainDTO result = trainService.createTrain(inputDTO);

        assertEquals(expectedDTO, result);
        verify(trainCache).clearAll();
    }

    @Test
    void getTrainById_Success() {
        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        when(trainRepository.findById(1L)).thenReturn(Optional.of(baseTrain));
        when(trainMapper.toDto(baseTrain)).thenReturn(expectedDTO);

        TrainDTO result = trainService.getTrainById(1L);

        assertEquals(expectedDTO, result);
    }

    @Test
    void updateTrainByNumber_PartialUpdate() {
        TrainDTO updateDTO = TrainDTO.builder()
                .arrivalStation("Kazan")
                .arrivalTime("22:00")
                .build();

        Train updatedTrain = Train.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("Kazan")
                .departureTime("10:00")
                .arrivalTime("22:00")
                .build();

        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("Kazan")
                .departureTime("10:00")
                .arrivalTime("22:00")
                .build();

        when(trainRepository.findByNumber("123")).thenReturn(Optional.of(baseTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(updatedTrain);
        when(trainMapper.toDto(updatedTrain)).thenReturn(expectedDTO);

        TrainDTO result = trainService.updateTrainByNumber("123", updateDTO);

        assertEquals("Kazan", result.getArrivalStation());
        assertEquals("22:00", result.getArrivalTime());
        verify(trainCache).clearAll();
    }

    @Test
    void updateTrainByNumber_ShouldThrowWhenTrainNotFound() {
        String trainNumber = "404";
        TrainDTO updateDTO = TrainDTO.builder()
                .arrivalStation("Kazan")
                .build();

        when(trainRepository.findByNumber(trainNumber)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> trainService.updateTrainByNumber(trainNumber, updateDTO));
    }

    @Test
    void updateTrainByNumber_ShouldUpdateOnlyDepartureStation() {
        String trainNumber = "123";
        TrainDTO updateDTO = TrainDTO.builder()
                .departureStation("Kazan")
                .build();

        Train updatedTrain = Train.builder()
                .id(1L)
                .number("123")
                .departureStation("Kazan")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Kazan")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        when(trainRepository.findByNumber(trainNumber)).thenReturn(Optional.of(baseTrain));
        when(trainRepository.save(any(Train.class))).thenReturn(updatedTrain);
        when(trainMapper.toDto(updatedTrain)).thenReturn(expectedDTO);

        TrainDTO result = trainService.updateTrainByNumber(trainNumber, updateDTO);

        assertEquals("Kazan", result.getDepartureStation());
        assertEquals("SPb", result.getArrivalStation()); // Original value
        verify(trainCache).clearAll();
    }

    @Test
    void updateTrainByNumber_ShouldNotUpdateWhenNullFields() {
        String trainNumber = "123";
        TrainDTO updateDTO = TrainDTO.builder().build(); // All fields null

        when(trainRepository.findByNumber(trainNumber)).thenReturn(Optional.of(baseTrain));
        when(trainRepository.save(baseTrain)).thenReturn(baseTrain);
        when(trainMapper.toDto(baseTrain)).thenReturn(baseTrainDTO);

        TrainDTO result = trainService.updateTrainByNumber(trainNumber, updateDTO);

        assertEquals(baseTrainDTO, result); // No changes
        verify(trainCache).clearAll();
    }

    @Test
    void findTrainsWithFreeSeats_WithSeats() {
        String cacheKey = "trains:Moscow:SPb";

        SeatDTO seatDTO = SeatDTO.builder()
                .id(1L)
                .trainId(1L)
                .number(1)
                .type("Business")
                .price(100.0)
                .isFree(true)
                .build();

        TrainDTO cachedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .seats(Collections.singletonList(seatDTO))
                .build();

        when(trainCache.getWithMetrics(cacheKey)).thenReturn(Collections.singletonList(cachedDTO));

        List<TrainDTO> result = trainService.findTrainsWithFreeSeats("Moscow", "SPb");

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSeats().size());
        verify(trainRepository, never()).findTrainsWithFreeSeatsNative(any(), any());
    }

    @Test
    void getAllTrains_WithSeats() {
        SeatDTO seatDTO = SeatDTO.builder()
                .id(1L)
                .trainId(1L)
                .number(1)
                .type("Business")
                .price(100.0)
                .isFree(true)
                .build();

        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .seats(Collections.singletonList(seatDTO))
                .build();

        when(trainRepository.findAll()).thenReturn(Collections.singletonList(baseTrain));
        when(trainMapper.toDto(baseTrain)).thenReturn(expectedDTO);

        List<TrainDTO> result = trainService.getAllTrains();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getSeats().size());
    }

    @Test
    void getTrainByNumber_Success() {
        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        when(trainRepository.findByNumber("123")).thenReturn(Optional.of(baseTrain));
        when(trainMapper.toDto(baseTrain)).thenReturn(expectedDTO);

        TrainDTO result = trainService.getTrainByNumber("123");

        assertEquals(expectedDTO, result);
    }

    @Test
    void searchByNumber_Success() {
        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        when(trainRepository.findByNumberContainingIgnoreCase("12"))
                .thenReturn(Collections.singletonList(baseTrain));
        when(trainMapper.toDto(baseTrain)).thenReturn(expectedDTO);

        List<TrainDTO> result = trainService.searchByNumber("12");

        assertEquals(1, result.size());
        assertEquals(expectedDTO, result.get(0));
    }

    @Test
    void deleteTrain_Success() {
        when(trainRepository.existsById(1L)).thenReturn(true);

        trainService.deleteTrain(1L);

        verify(trainRepository).deleteById(1L);
        verify(trainCache).clearAll();
    }

    @Test
    void deleteTrain_ShouldThrowWhenTrainNotFound() {
        Long nonExistentId = 999L;
        when(trainRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class,
                () -> trainService.deleteTrain(nonExistentId));
        verify(trainRepository, never()).deleteById(any());
        verify(trainCache, never()).clearAll();
    }

    @Test
    void findTrainsWithFreeSeats_DBQuery() {
        String cacheKey = "trains:Moscow:SPb";
        TrainDTO expectedDTO = TrainDTO.builder()
                .id(1L)
                .number("123")
                .departureStation("Moscow")
                .arrivalStation("SPb")
                .departureTime("10:00")
                .arrivalTime("20:00")
                .build();

        when(trainCache.getWithMetrics(cacheKey)).thenReturn(null);
        when(trainRepository.findTrainsWithFreeSeatsNative("Moscow", "SPb"))
                .thenReturn(Collections.singletonList(baseTrain));
        when(trainMapper.toDto(baseTrain)).thenReturn(expectedDTO);

        List<TrainDTO> result = trainService.findTrainsWithFreeSeats("Moscow", "SPb");

        assertEquals(1, result.size());
        assertEquals(expectedDTO, result.get(0));
        verify(trainCache).put(cacheKey, result);
    }
}