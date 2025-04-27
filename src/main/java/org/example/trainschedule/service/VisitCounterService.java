package org.example.trainschedule.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class VisitCounterService {
    private final ConcurrentHashMap<String, AtomicLong> urlCounters = new ConcurrentHashMap<>();

    public void incrementCount(String url) {
        urlCounters.computeIfAbsent(url, k -> new AtomicLong(0)).incrementAndGet();
    }

    public long getCount(String url) {
        return urlCounters.getOrDefault(url, new AtomicLong(0)).get();
    }

    public ConcurrentHashMap<String, AtomicLong> getAllCounts() {
        return new ConcurrentHashMap<>(urlCounters);
    }
}
