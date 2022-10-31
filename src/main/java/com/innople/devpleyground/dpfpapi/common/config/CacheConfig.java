package com.innople.devpleyground.dpfpapi.common.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;

@Profile("local")
@Configuration
@EnableCaching
@EnableScheduling
@Log4j2
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(
                new ConcurrentMapCache("loadUserByUsername")));
        return cacheManager;
    }

    // loadUserByUserName, loadUserByUserNameAndGroupName 에 대한 캐시 삭제 스케줄 추가, (60 * 1000 tick) = 1분
    @CacheEvict(allEntries = true, value = {"loadUserByUsername"})
    @Scheduled(fixedDelay = 15 * 60 * 1000 /* 15분 */,  initialDelay = 500)
    public void reportCacheEvict() { log.info("Cache about UserDetails has been flushed"); }
}
