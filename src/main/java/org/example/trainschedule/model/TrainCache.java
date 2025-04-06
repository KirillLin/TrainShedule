package org.example.trainschedule.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.example.trainschedule.dto.TrainDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TrainCache {
    @SuppressWarnings("checkstyle:ConstantName")
    private static final Logger logger = LoggerFactory.getLogger(TrainCache.class);
    private final Map<String, CacheEntry> cache = new LinkedHashMap<>();
    private static final int MAX_CACHE_SIZE = 100;

    private static class CacheEntry {
        List<TrainDTO> data;
        long creationTime;

        CacheEntry(List<TrainDTO> data) {
            this.data = data;
            this.creationTime = System.currentTimeMillis();
        }
    }

    public synchronized List<TrainDTO> getWithMetrics(String key) {
        long startTime = System.currentTimeMillis();

        if (cache.containsKey(key)) {
            CacheEntry entry = cache.get(key);
            long duration = System.currentTimeMillis() - startTime;
            logger.info("Данные получены из кэша за {} мс | Ключ: {}", duration, key);
            return entry.data;
        }

        long duration = System.currentTimeMillis() - startTime;
        logger.info("Промах кэша за {} мс | Ключ: {}", duration, key);
        return null;
    }

    public synchronized void put(String key, List<TrainDTO> trains) {
        long startTime = System.currentTimeMillis();

        if (cache.size() >= MAX_CACHE_SIZE) {
            Iterator<String> iterator = cache.keySet().iterator();
            if (iterator.hasNext()) {
                String oldestKey = iterator.next();
                cache.remove(oldestKey);
                logger.debug("Вытеснен старый ключ из кэша: {}", oldestKey);
            }
        }

        cache.put(key, new CacheEntry(trains));
        long duration = System.currentTimeMillis() - startTime;

        logger.info("Данные добавлены в кэш за {} мс | Ключ: {} | Размер кэша: {}",
                duration, key, cache.size());
    }

    public synchronized boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public synchronized void clearAll() {
        int sizeBefore = cache.size();
        cache.clear();
        logger.info("Кэш полностью очищен | Размер до очистки: {}", sizeBefore);
    }

    public synchronized void logCacheStats() {
        logger.info("Текущий размер кэша: {}", cache.size());
        logger.info("Содержимое кэша: {}", cache.keySet());
    }
}
