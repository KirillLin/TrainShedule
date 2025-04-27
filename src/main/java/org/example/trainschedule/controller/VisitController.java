package org.example.trainschedule.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.example.trainschedule.service.VisitCounterService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visits")
@Tag(name = "Visit Management", description = "API for counting visits")
public class VisitController {
    private final VisitCounterService visitCounterService;

    public VisitController(VisitCounterService visitCounterService) {
        this.visitCounterService = visitCounterService;
    }

    @Operation(summary = "Counting for endpoint")
    @GetMapping("/count/{url}")
    public long getVisitCount(
            @RequestParam("url") String url) {
        return visitCounterService.getCount(url);
    }

    @GetMapping("/all")
    @Operation(summary = "Counting all")
    public ConcurrentHashMap<String, AtomicLong> getAllVisits() {
        return visitCounterService.getAllCounts();
    }
}