package org.example.trainschedule.model;

import java.nio.file.Path;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LogTask {
    private String taskId;
    private TaskStatus status;
    private Path resultFile;
    private String errorMessage;
    private String date;

    public enum TaskStatus {
        PENDING, PROCESSING, COMPLETED, FAILED
    }
}
