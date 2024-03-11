package ru.shadi777.proxyapplication.configuration;


import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Log4j2
@Configuration
@EnableCaching
@EnableScheduling
public class CacheConfiguration {
    public static final String PROXY_CACHE = "proxy";

    @Scheduled(fixedDelayString = "${app.cache.clear-interval}")
    @CacheEvict(value = PROXY_CACHE, allEntries = true)
    public void clearProxyCache() {
        log.info("Clearing proxy caches...");
    }
}
