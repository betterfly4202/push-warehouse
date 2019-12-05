package com.wemakeprice.push.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemakeprice.push.model.RedisSample;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */

@Configuration
public class RedisRepositoryConfiguration {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisHost, redisPort);
        factory.setPassword(redisPassword);

        return factory;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisFactory = new JedisConnectionFactory();
        jedisFactory.setHostName(redisHost);
        jedisFactory.setPort(redisPort);
        jedisFactory.setPassword(redisPassword);
        jedisFactory.setUsePool(true);
        return jedisFactory;
    }


    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisSample.class));
        redisTemplate.setValueSerializer(new JsonRedisSerializer());

        return redisTemplate;
    }

    static class JsonRedisSerializer implements RedisSerializer<Object> {


        private final ObjectMapper om;


        public JsonRedisSerializer() {
            this.om = new ObjectMapper()
                    .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

            this.om.registerSubtypes(RedisSample.class);
        }


        @Override
        public byte[] serialize(Object t) throws SerializationException {
            try {
                return om.writeValueAsBytes(t);
            } catch (JsonProcessingException e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }


        @Override
        public Object deserialize(byte[] bytes) throws SerializationException {

            if(bytes == null){
                return null;
            }

            try {
                return om.readValue(bytes, Object.class);
            } catch (Exception e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }
    }
}