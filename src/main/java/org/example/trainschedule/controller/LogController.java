package org.example.trainschedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import org.example.trainschedule.service.LogService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Log Management", description = "API for accessing application logs")
public class LogController {
    private final LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @Operation(summary = "Download logs by date",
            description = "Download application logs filtered by a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logs downloaded successfully",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(type = "string", format = "binary"))),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "404", description = "Log file not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadLogsByDate(
            @Parameter(description = "Date in YYYY-MM-DD format", required = true,
                    example = "2023-12-31")
            @RequestParam @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date) {
        try {
            return logService.getLogsByDateAsDownloadableFile(date);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest()
                    .body(null);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound()
                    .build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(null);
        }
    }
}