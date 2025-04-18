package org.example.trainschedule.Service;

import org.example.trainschedule.cache.TrainCache;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.mappers.SeatMapper;
import org.example.trainschedule.model.Seat;
import org.example.trainschedule.model.Train;
import org.example.trainschedule.repository.SeatRepository;
import org.example.trainschedule.repository.TrainRepository;
import org.example.trainschedule.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private SeatMapper seatMapper;

    @Mock
    private TrainCache trainCache;

    @InjectMocks
    private SeatService seatService;

    private final Long TEST_SEAT_ID = 1L;
    private final Long TEST_TRAIN_ID = 1L;
    private Seat testSeat;
    private SeatDTO testSeatDTO;

    @BeforeEach
    void setUp() {
        testSeat = Seat.builder()
                .id(TEST_SEAT_ID)
                .number(1)
                .isFree(true)
                .type("Business")
                .price(100.0)
                .build();

        testSeatDTO = SeatDTO.builder()
                .id(TEST_SEAT_ID)
                .trainId(TEST_TRAIN_ID)
                .number(1)
                .isFree(true)
                .type("Business")
                .price(100.0)
                .build();
    }

    @Test
    void getAllSeats_ShouldReturnListOfSeats() {
        when(seatRepository.findAll()).thenReturn(List.of(testSeat));
        when(seatMapper.toDto(testSeat)).thenReturn(testSeatDTO);

        List<SeatDTO> result = seatService.getAllSeats();

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(SeatDTO::getId, SeatDTO::getNumber)
                .containsExactly(TEST_SEAT_ID, 1);
    }

    @Test
    void getSeatById_WhenSeatExists_ShouldReturnSeat() {
        when(seatRepository.findById(TEST_SEAT_ID)).thenReturn(Optional.of(testSeat));
        when(seatMapper.toDto(testSeat)).thenReturn(testSeatDTO);

        SeatDTO result = seatService.getSeatById(TEST_SEAT_ID);

        assertThat(result)
                .extracting(SeatDTO::getId, SeatDTO::getNumber)
                .containsExactly(TEST_SEAT_ID, 1);
    }

    @Test
    void getSeatById_WhenSeatNotExists_ShouldThrowException() {
        when(seatRepository.findById(TEST_SEAT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> seatService.getSeatById(TEST_SEAT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Seat not found with id: " + TEST_SEAT_ID);
    }

    @Test
    void getSeatsByTrainId_ShouldReturnFilteredSeats() {
        // Given
        when(seatRepository.findByTrainId(TEST_TRAIN_ID)).thenReturn(List.of(testSeat));
        when(seatMapper.toDto(testSeat)).thenReturn(testSeatDTO);

        List<SeatDTO> result = seatService.getSeatsByTrainId(TEST_TRAIN_ID);

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(SeatDTO::getTrainId, SeatDTO::getNumber)
                .containsExactly(TEST_TRAIN_ID, 1);
    }

    @Test
    void updateSeat_WhenValidData_ShouldReturnUpdatedSeat() {
        SeatDTO updatedDTO = SeatDTO.builder()
                .trainId(TEST_TRAIN_ID)
                .number(2)
                .isFree(false)
                .type("Economy")
                .price(50.0)
                .build();

        Train train = Train.builder().id(TEST_TRAIN_ID).build();
        Seat updatedSeat = Seat.builder().id(TEST_SEAT_ID).train(train).build();

        when(seatRepository.findById(TEST_SEAT_ID)).thenReturn(Optional.of(testSeat));
        when(trainRepository.findById(TEST_TRAIN_ID)).thenReturn(Optional.of(train));
        when(seatRepository.existsByTrainIdAndNumber(TEST_TRAIN_ID, 2)).thenReturn(false);
        when(seatRepository.save(any())).thenReturn(updatedSeat);
        when(seatMapper.toDto(updatedSeat)).thenReturn(updatedDTO);

        SeatDTO result = seatService.updateSeat(TEST_SEAT_ID, updatedDTO);

        assertThat(result)
                .extracting(SeatDTO::getNumber, SeatDTO::getIsFree)
                .containsExactly(2, false);
        verify(trainCache).clearAll();
    }

    @Test
    void updateSeat_WhenDuplicateNumber_ShouldThrowException() {
        SeatDTO updatedDTO = SeatDTO.builder()
                .trainId(TEST_TRAIN_ID)
                .number(2)
                .build();

        Train train = Train.builder().id(TEST_TRAIN_ID).build();

        when(seatRepository.findById(TEST_SEAT_ID)).thenReturn(Optional.of(testSeat));
        when(trainRepository.findById(TEST_TRAIN_ID)).thenReturn(Optional.of(train));
        when(seatRepository.existsByTrainIdAndNumber(TEST_TRAIN_ID, 2)).thenReturn(true);

        assertThatThrownBy(() -> seatService.updateSeat(TEST_SEAT_ID, updatedDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("already exists in this train");
    }

    @Test
    void addSeatToTrain_WhenValidData_ShouldReturnSavedSeat() {
        Train train = Train.builder().id(TEST_TRAIN_ID).build();
        Seat savedSeat = Seat.builder().id(TEST_SEAT_ID).train(train).build();

        when(trainRepository.findById(TEST_TRAIN_ID)).thenReturn(Optional.of(train));
        when(seatRepository.save(any())).thenReturn(savedSeat);
        when(seatMapper.toDto(savedSeat)).thenReturn(testSeatDTO);

        SeatDTO result = seatService.addSeatToTrain(TEST_TRAIN_ID, testSeatDTO);

        assertThat(result.getId()).isEqualTo(TEST_SEAT_ID);
        verify(trainCache).clearAll();
    }

    @Test
    void addSeatToTrain_WhenTrainIdMismatch_ShouldThrowException() {
        SeatDTO invalidDto = SeatDTO.builder().trainId(999L).build();

        assertThatThrownBy(() -> seatService.addSeatToTrain(TEST_TRAIN_ID, invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Train ID in path and body must match");
    }

    @Test
    void deleteSeat_WhenSeatExists_ShouldDelete() {
        when(seatRepository.existsById(TEST_SEAT_ID)).thenReturn(true);

        seatService.deleteSeat(TEST_SEAT_ID);

        verify(seatRepository).deleteById(TEST_SEAT_ID);
        verify(trainCache).clearAll();
    }

    @Test
    void deleteSeat_WhenSeatNotExists_ShouldThrowException() {
        when(seatRepository.existsById(TEST_SEAT_ID)).thenReturn(false);

        assertThatThrownBy(() -> seatService.deleteSeat(TEST_SEAT_ID))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Seat not found with id: " + TEST_SEAT_ID);
    }
}