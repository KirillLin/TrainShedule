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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    void createTrains_ShouldCreateOnlyNewTrains() {
        TrainDTO existingTrain = TrainDTO.builder().number("T001").build();
        TrainDTO newTrain = TrainDTO.builder().number("T002").build();

        when(trainRepository.findAllNumbers()).thenReturn(Set.of("T001"));
        when(trainService.createTrain(newTrain)).thenReturn(newTrain);

        List<TrainDTO> result = bulkService.createTrains(Arrays.asList(existingTrain, newTrain));

        assertEquals(1, result.size());
        assertEquals("T002", result.get(0).getNumber());
        verify(trainService, never()).createTrain(existingTrain);
        verify(trainService, times(1)).createTrain(newTrain);
    }

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
    void createTrains_ShouldReturnSuccessfullyCreatedTrainsWhenSomeFail() {
        TrainDTO validTrain = TrainDTO.builder().number("T001").build();
        TrainDTO invalidTrain = TrainDTO.builder().number("T002").build();

        when(trainRepository.findAllNumbers()).thenReturn(Collections.emptySet());
        when(trainService.createTrain(validTrain)).thenReturn(validTrain);
        when(trainService.createTrain(invalidTrain)).thenThrow(new IllegalArgumentException());

        List<TrainDTO> result = bulkService.createTrains(Arrays.asList(validTrain, invalidTrain));

        assertEquals(1, result.size());
        assertEquals("T001", result.get(0).getNumber());
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
}