package org.example.trainschedule.Service;

import org.example.trainschedule.service.LogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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

    private static class TestableLogService extends LogService {
        @Override
        protected Path getLogFilePath() {
            return super.getLogFilePath();
        }
    }

    private final TestableLogService logService = new TestableLogService();

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

    @Test
    void getLogsByDateAsDownloadableFile_ShouldHaveCorrectHeaders() throws IOException {
        String testDate = "2025-01-01";
        Path logFile = tempDir.resolve("application.log");
        try (BufferedWriter writer = Files.newBufferedWriter(logFile)) {
            writer.write("2025-01-01 Test line\n");
        }

        LogService service = new TestLogService(logFile);
        ResponseEntity<Resource> response = service.getLogsByDateAsDownloadableFile(testDate);

        assertEquals(MediaType.TEXT_PLAIN, response.getHeaders().getContentType());
        assertTrue(response.getHeaders().getContentLength() > 0);
    }

    @Test
    void isLineMatchesDate_ShouldReturnTrueForMatchingDate() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String validLine = "2023-05-15 This log entry matches the target date";

        assertTrue(logService.isLineMatchesDate(validLine, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForNonMatchingDate() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String nonMatchingLine = "2023-05-16 This log entry doesn't match";

        assertFalse(logService.isLineMatchesDate(nonMatchingLine, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForNullLine() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);

        assertFalse(logService.isLineMatchesDate(null, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForShortLine() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String shortLine = "2023-05";

        assertFalse(logService.isLineMatchesDate(shortLine, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForInvalidDateFormat() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String invalidFormatLine = "15/05/2023 Wrong date format";

        assertFalse(logService.isLineMatchesDate(invalidFormatLine, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForInvalidDate() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String invalidDateLine = "2023-13-01 Invalid month in date";

        assertFalse(logService.isLineMatchesDate(invalidDateLine, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForEmptyLine() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String emptyLine = "";

        assertFalse(logService.isLineMatchesDate(emptyLine, targetDate));
    }

    @Test
    void isLineMatchesDate_ShouldReturnFalseForLineWithOnlyDate() {
        LocalDate targetDate = LocalDate.of(2023, 5, 15);
        String dateOnlyLine = "2023-05-16"; // Ровно 10 символов, но не совпадает

        assertFalse(logService.isLineMatchesDate(dateOnlyLine, targetDate));
    }

    @Test
    void getLogFilePath_ShouldReturnCorrectPath() {
        Path expectedPath = Paths.get("logs/application.log");
        Path actualPath = logService.getLogFilePath();

        assertEquals(expectedPath, actualPath);
    }

    @Test
    void getLogFilePath_ShouldReturnNonNullPath() {
        Path path = logService.getLogFilePath();

        assertNotNull(path);
    }

}