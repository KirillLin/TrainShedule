package org.example.trainschedule.Service;

import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.repository.TrainRepository;
import org.example.trainschedule.service.BulkService;
import org.example.trainschedule.service.TrainService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkServiceTest {

    @Mock
    private TrainService trainService;

    @Mock
    private TrainRepository trainRepository;

    @InjectMocks
    private BulkService bulkService;

    @Test
    void createTrains_ShouldReturnEmptyListWhenAllExist() {
        TrainDTO train1 = TrainDTO.builder().number("T001").build();
        TrainDTO train2 = TrainDTO.builder().number("T002").build();

        when(trainRepository.findAllNumbers()).thenReturn(new HashSet<>(Arrays.asList("T001", "T002")));

        List<TrainDTO> result = bulkService.createTrains(Arrays.asList(train1, train2));

        assertTrue(result.isEmpty());
        verify(trainService, never()).createTrain(any());
    }

    @Test
    void createTrains_ShouldHandleEmptyList() {
        List<TrainDTO> result = bulkService.createTrains(Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(trainRepository);
        verifyNoInteractions(trainService);
    }

    @Test
    void createTrains_ShouldHandleNullInput() {
        List<TrainDTO> result = bulkService.createTrains(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(trainRepository);
        verifyNoInteractions(trainService);
    }

    @Test
    void createTrains_trainCreationFails_logsErrorAndSkipsTrain() {
        TrainDTO trainDTO1 = TrainDTO.builder().number("123A").departureStation("A").arrivalStation("B").departureTime("10:00").arrivalTime("12:00").build();
        TrainDTO trainDTO2 = TrainDTO.builder().number("456B").departureStation("C").arrivalStation("D").departureTime("14:00").arrivalTime("16:00").build();

        List<TrainDTO> trainDTOs = Arrays.asList(trainDTO1, trainDTO2);

        Set<String> existingNumbers = new HashSet<>();
        when(trainRepository.findAllNumbers()).thenReturn(existingNumbers);

        when(trainService.createTrain(trainDTO1)).thenThrow(new RuntimeException("Creation failed"));
        TrainDTO createdTrainDTO2 = TrainDTO.builder().id(2L).number("456B").departureStation("C").arrivalStation("D").departureTime("14:00").arrivalTime("16:00").build();
        when(trainService.createTrain(trainDTO2)).thenReturn(createdTrainDTO2);

        List<TrainDTO> result = bulkService.createTrains(trainDTOs);

        assertEquals(1, result.size());
        assertEquals(createdTrainDTO2, result.get(0));

        verify(trainService, times(1)).createTrain(trainDTO1);
        verify(trainService, times(1)).createTrain(trainDTO2);
    }
}