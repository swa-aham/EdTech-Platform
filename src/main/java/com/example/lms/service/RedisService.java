package com.example.lms.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public <T> T get(String key, Class<T> entityClass) {
        try {
            Object object = redisTemplate.opsForValue().get(key);
            if (object == null) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(object.toString(), entityClass);
        } catch (Exception e) {
            log.error("Exception in Redis get operation", e);
            return null;
        }
    }

    public void set(String key, Object value, Long ttl) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonValue = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("Exception in Redis set operation", e);
        }
    }

    public boolean delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.info("Deleted cache for key: {}, result: {}", key, result);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("Exception in Redis delete operation", e);
            return false;
        }
    }

    public Long deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long deletedCount = redisTemplate.delete(keys);
                log.info("Deleted {} keys matching pattern: {}", deletedCount, pattern);
                return deletedCount;
            }
            return 0L;
        } catch (Exception e) {
            log.error("Exception in Redis pattern delete operation", e);
            return 0L;
        }
    }
}