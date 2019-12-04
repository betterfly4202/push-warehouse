package com.wemakeprice.push.service;

import com.wemakeprice.push.model.RedisSample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.12.02
 */

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, RedisSample> redisTemplate;

    public void set(String key, RedisSample data){
        redisTemplate.opsForValue().set(key, data);
    }

    public RedisSample get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key){
        redisTemplate.delete(key);
    }

    public boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }
}
