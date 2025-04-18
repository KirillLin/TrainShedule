package org.example.trainschedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.service.BulkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bulk")
@Tag(name = "Bulk Operations", description = "API for bulk operations with trains and seats")
public class BulkController {
    private final BulkService bulkService;

    public BulkController(BulkService bulkService) {
        this.bulkService = bulkService;
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Operation(summary = "Create multiple trains",
            description = "Add several trains to the system in one operation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trains created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/trains")
    public ResponseEntity<List<TrainDTO>> createTrains(
            @Valid @RequestBody List<TrainDTO> trainDTOs) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bulkService.createTrains(trainDTOs));
    }
}


