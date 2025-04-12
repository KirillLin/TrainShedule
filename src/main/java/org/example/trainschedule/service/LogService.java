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
import java.util.stream.Stream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private static final String LOG_FILE_PATH = "logs/application.log";
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
        Path sourcePath = Paths.get(LOG_FILE_PATH);

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

    private boolean isLineMatchesDate(String line, LocalDate targetDate) {
        if (line == null || line.length() < 10) return false;

        try {
            String lineDate = line.substring(0, 10);
            return LocalDate.parse(lineDate, DATE_FORMATTER).equals(targetDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
