package org.example.trainschedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SuppressWarnings("checkstyle:RegexpMultiline")
@SpringBootApplication
@EnableTransactionManagement
public class TrainScheduleApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainScheduleApplication.class, args);
    }

}

