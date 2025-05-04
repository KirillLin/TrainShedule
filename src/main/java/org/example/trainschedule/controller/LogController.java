package org.example.trainschedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.example.trainschedule.dto.TrainDTO;
import org.example.trainschedule.model.LogTask;
import org.example.trainschedule.service.LogService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/generate")
    @Operation(summary = "Start log generation",
            description = "Start asynchronous log file generation")
    public ResponseEntity<String> generateLogFile(
            @RequestParam @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String date) {
        LogTask task = logService.createTask(date);
        logService.processLogTaskAsync(task);
        return ResponseEntity.accepted().body(task.getTaskId());
    }

    @GetMapping("/status/{taskId}")
    @Operation(summary = "Check generation status")
    public ResponseEntity<LogTask> getStatus(@PathVariable String taskId) {
        LogTask task = logService.getTaskStatus(taskId);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/download/{taskId}")
    @Operation(
            summary = "Download generated logs",
            description = "Returns the log file if ready (200)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File is Ready"),
            @ApiResponse(responseCode = "400", description = "File in process"),
            @ApiResponse(responseCode = "404", description = "This task dont exist")
    })
    public ResponseEntity<Resource> downloadResult(@PathVariable String taskId) {
        try {
            return logService.getTaskResult(taskId);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("Retry-After", "30")
                    .body(new ByteArrayResource("The file is still being created".getBytes()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}