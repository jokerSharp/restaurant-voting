package org.example.restaurantvoting.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    public static final String RESTAURANT_CACHE_NAME = "restaurants";

    @Autowired
    private CacheManager cacheManager;

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearCache() {
        cacheManager.getCache(RESTAURANT_CACHE_NAME).clear();
        log.info("{} cache is cleared by the schedule", RESTAURANT_CACHE_NAME);
    }
}
