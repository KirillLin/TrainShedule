package org.example.trainschedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.trainschedule.dto.SeatDTO;
import org.example.trainschedule.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Validated
@Tag(name = "Seat Management", description = "API for managing train seats")
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "Get all seats", description = "Retrieve a list of all available seats")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of seats",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SeatDTO.class)))
    @GetMapping
    public ResponseEntity<List<SeatDTO>> getAllSeats() {
        return ResponseEntity.ok(seatService.getAllSeats());
    }

    @Operation(summary = "Get seat by ID", description = "Retrieve a specific seat by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved seat",
                    content = @Content(schema = @Schema(implementation = SeatDTO.class))),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(seatService.getSeatById(id));
    }

    @Operation(summary = "Get seats by train ID",
            description = "Retrieve all seats belonging to a specific train")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of seats",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = SeatDTO.class)))
    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<SeatDTO>> getSeatsByTrainId(@PathVariable Long trainId) {
        return ResponseEntity.ok(seatService.getSeatsByTrainId(trainId));
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Operation(summary = "Add seat to train",
            description = "Create a new seat and assign it to a specific train")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Seat created successfully",
                    content = @Content(schema = @Schema(implementation = SeatDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Train not found")
    })
    @PostMapping("/{trainId}")
    public ResponseEntity<SeatDTO> addSeat(
            @PathVariable Long trainId,
            @Valid @RequestBody SeatDTO seatDTO) {
        seatDTO.setTrainId(trainId);
        SeatDTO createdSeat = seatService.addSeatToTrain(trainId, seatDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeat);
    }

    @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
    @Operation(summary = "Update seat", description = "Update information about a specific seat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seat updated successfully",
                    content = @Content(schema = @Schema(implementation = SeatDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<SeatDTO> updateSeat(
            @PathVariable Long id,
            @Valid @RequestBody SeatDTO seatDTO) {
        return ResponseEntity.ok(seatService.updateSeat(id, seatDTO));
    }

    @Operation(summary = "Delete seat", description = "Remove a seat from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Seat deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Seat not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.noContent().build();
    }
}