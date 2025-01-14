package com.yen.mdblog.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/** Cache service created by gpt */
@Service
public class CacheService {

    /**
     * Generic method to cache a result.
     *
     * @param cacheName the name of the cache
     * @param key       the cache key
     * @param operation the operation to cache
     * @param <T>       the type of the cached result
     * @return the cached result
     */
    @Cacheable(value = "#{cacheName}", key = "#{key}")
    public <T> T cacheResult(String cacheName, String key, CacheableOperation<T> operation) {
        return operation.execute();
    }

    @FunctionalInterface
    public interface CacheableOperation<T> {
        T execute();
    }
}