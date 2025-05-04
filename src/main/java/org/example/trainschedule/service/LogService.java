package org.example.trainschedule.service;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;
import org.example.trainschedule.model.LogTask;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private static final String LOG_FILE_PATH = "logs/application.log";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final ConcurrentMap<String, LogTask> tasks = new ConcurrentHashMap<>();

    public ResponseEntity<Resource> getLogsByDateAsDownloadableFile(String dateString) throws IOException {
        LocalDate targetDate = LocalDate.parse(dateString, DATE_FORMATTER);

        Path tempFile = createFilteredLogFile(targetDate);

        Resource resource = new InputStreamResource(Files.newInputStream(tempFile));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=logs-" + dateString + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(Files.size(tempFile))
                .body(resource);
    }

    @SuppressWarnings({"checkstyle:Indentation", "checkstyle:RegexpMultiline"})
    private Path createFilteredLogFile(LocalDate targetDate) throws IOException {
        Path sourcePath = getLogFilePath();

        if (!Files.exists(sourcePath)) {
            throw new FileNotFoundException("Log file not found at: " + sourcePath);
        }

        Path tempFile = Files.createTempFile("logs-filtered-", ".log");

        try (Stream<String> lines = Files.lines(sourcePath, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            lines.filter(line -> isLineMatchesDate(line, targetDate))
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }

        tempFile.toFile().deleteOnExit();

        return tempFile;
    }

    public boolean isLineMatchesDate(String line, LocalDate targetDate) {
        if (line == null || line.length() < 10) return false;

        try {
            String lineDate = line.substring(0, 10);
            return LocalDate.parse(lineDate, DATE_FORMATTER).equals(targetDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    protected Path getLogFilePath() {
        return Paths.get(LOG_FILE_PATH);
    }

    @Async
    public void processLogTaskAsync(LogTask task) {
        try {
            task.setStatus(LogTask.TaskStatus.PROCESSING);

            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Path tempFile = createFilteredLogFile(LocalDate.parse(task.getDate()));
            task.setResultFile(tempFile);
            task.setStatus(LogTask.TaskStatus.COMPLETED);
        } catch (Exception e) {
            task.setStatus(LogTask.TaskStatus.FAILED);
            task.setErrorMessage(e.getMessage());
        }
    }

    public LogTask getTaskStatus(String taskId) {
        return tasks.get(taskId);
    }

    public ResponseEntity<Resource> getTaskResult(String taskId) throws IOException {
        LogTask task = tasks.get(taskId);
        if (task == null || task.getResultFile() == null) {
            throw new FileNotFoundException("Task result not available");
        }

        Resource resource = new InputStreamResource(Files.newInputStream(task.getResultFile()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=logs-" + task.getDate() + ".log")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(Files.size(task.getResultFile()))
                .body(resource);
    }

    public LogTask createTask(String date) {
        LogTask task = new LogTask();
        task.setTaskId(UUID.randomUUID().toString());
        task.setStatus(LogTask.TaskStatus.PENDING);
        task.setDate(date);
        tasks.put(task.getTaskId(), task);
        return task;
    }
}
