package org.example.trainschedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.service.TrainService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trains")
@Tag(name = "Train Management", description = "API for managing trains")
@Validated
@RequiredArgsConstructor
public class TrainController {
    private final TrainService trainService;

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Operation(summary = "Create train", description = "Add a new train to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Train created successfully",
                    content = @Content(schema = @Schema(implementation = TrainDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<TrainDTO> createTrain(@Valid @RequestBody TrainDTO trainDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trainService.createTrain(trainDTO));
    }

    @Operation(summary = "Get train by ID", description = "Retrieve a specific train by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved train",
                    content = @Content(schema = @Schema(implementation = TrainDTO.class))),
            @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TrainDTO> getTrainById(@PathVariable Long id) {
        return ResponseEntity.ok(trainService.getTrainById(id));
    }

    @Operation(summary = "Get all trains", description = "Retrieve a list of all available trains")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of trains",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainDTO.class)))
    @GetMapping
    public ResponseEntity<List<TrainDTO>> getAllTrains() {
        return ResponseEntity.ok(trainService.getAllTrains());
    }

    @Operation(summary = "Search trains by number",
            description = "Find trains by their number (partial match supported)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved matching trains",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainDTO.class)))
    @GetMapping("/search")
    public ResponseEntity<List<TrainDTO>> searchTrains(
            @Parameter(description = "Train number or part of it", required = true)
            @RequestParam @NotBlank String number) {
        return ResponseEntity.ok(trainService.searchByNumber(number));
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Operation(summary = "Update train by number",
            description = "Update information about a specific train identified by its number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Train updated successfully",
                    content = @Content(schema = @Schema(implementation = TrainDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @PutMapping("/by-number/{trainNumber}")
    public ResponseEntity<TrainDTO> updateTrainByNumber(
            @Parameter(description = "Train number to update", required = true)
            @PathVariable String trainNumber,
            @Valid @RequestBody TrainDTO trainDTO) {
        return ResponseEntity.ok(trainService.updateTrainByNumber(trainNumber, trainDTO));
    }

    @Operation(summary = "Delete train", description = "Remove a train from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Train deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrain(@PathVariable Long id) {
        trainService.deleteTrain(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Find trains with free seats from A to B",
            description = "Search for trains with available seats between two stations")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved matching trains",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = TrainDTO.class)))
    @GetMapping("/with-free-seats")
    public ResponseEntity<List<TrainDTO>> getTrainsWithFreeSeats(
            @Parameter(description = "Departure station", required = true)
            @RequestParam @NotBlank String departure,
            @Parameter(description = "Arrival station", required = true)
            @RequestParam @NotBlank String arrival) {
        return ResponseEntity.ok(trainService.findTrainsWithFreeSeats(departure, arrival));
    }
}
