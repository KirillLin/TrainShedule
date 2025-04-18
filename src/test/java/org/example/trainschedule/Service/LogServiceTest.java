package org.example.trainschedule.Service;

import org.example.trainschedule.service.LogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LogServiceTest {

    private static class TestLogService extends LogService {
        private final Path logFilePath;

        TestLogService(Path logFilePath) {
            this.logFilePath = logFilePath;
        }

        @Override
        protected Path getLogFilePath() {
            return logFilePath;
        }
    }

    @TempDir
    Path tempDir;

    @Test
    void getLogsByDateAsDownloadableFile_ShouldReturnFilteredLogs() throws IOException {
        String testDate = "2025-01-01";
        Path logFile = tempDir.resolve("application.log");

        try (BufferedWriter writer = Files.newBufferedWriter(logFile)) {
            writer.write("2025-01-01 Line 1\n");
            writer.write("2025-01-01 Line 2\n");
            writer.write("2025-01-02 Other date\n");
        }

        LogService service = new TestLogService(logFile);

        ResponseEntity<Resource> response = service.getLogsByDateAsDownloadableFile(testDate);

        assertNotNull(response);
        assertEquals("attachment; filename=logs-2025-01-01.log",
                response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        String content = StreamUtils.copyToString(response.getBody().getInputStream(), StandardCharsets.UTF_8);
        assertTrue(content.contains("Line 1"));
        assertTrue(content.contains("Line 2"));
        assertFalse(content.contains("Other date"));
    }

    @Test
    void getLogsByDateAsDownloadableFile_ShouldThrowWhenFileNotFound() {
        LogService service = new TestLogService(Paths.get("nonexistent.log"));

        assertThrows(FileNotFoundException.class,
                () -> service.getLogsByDateAsDownloadableFile("2025-01-01"));
    }

    @Test
    void getLogsByDateAsDownloadableFile_ShouldThrowOnInvalidDateFormat() {
        LogService service = new TestLogService(Paths.get("any.log"));

        assertThrows(DateTimeParseException.class,
                () -> service.getLogsByDateAsDownloadableFile("invalid-date"));
    }
}