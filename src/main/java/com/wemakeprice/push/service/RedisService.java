package com.wemakeprice.push.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.12.02
 */

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object data){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);
    }

    public Object get(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        return valueOperations.get(key);
    }
}
