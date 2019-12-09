package com.wemakeprice.push.model;

import com.wemakeprice.push.repository.RedisSampleRedisRepository;
import com.wemakeprice.push.service.RedisService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisSampleTest{

    @Autowired
    private RedisSampleRedisRepository repository;

    @Autowired
    private RedisService redisService;

    @After
    public void tearDown(){
        repository.deleteAll();
    }

    private RedisSample sample;
    private Push push;
    private String id = "betterFLY";

    @Before
    public void init(){
        //given
        LocalDateTime now = LocalDateTime.now();
        sample = RedisSample.builder()
                .id(id)
                .refreshTime(now)
                .point(1_000L)
                .build();

        push = Push.builder()
                .id(1L)
                .contents("SAMPLE")
                .token(Arrays.asList("AA-bbcc", "xxxx-ASDW", "120391a-AA"))
                .refreshTime(now)
                .build();
    }

    @Test
    public void 레디스_등록_조회(){
        //when
        repository.save(sample);

        //then
        RedisSample loadRedis = repository.findById(id).get();
        assertThat(loadRedis.getPoint(), is(1000L));
    }

    @Test
    public void 레디스_등록_수정(){
        //when
        repository.save(sample);
        RedisSample loadRedis = repository.findById(id).get();
        loadRedis.setPoint(3_0000L);
        repository.save(loadRedis);

        //then
        assertThat(loadRedis.getPoint(), is(30000L));
    }

    @Test
    public void 레디스_Set(){
        redisService.set("key_15", sample);
        redisService.set("push_1", push);
        ;
    }

    @Test
    public void 레디스_Get() {
        assertThat(redisService.get("key_15").getPoint(), is(1_000L));
        assertThat(redisService.pushGet("push_1").getId(), is(1L));
//        System.out.println(redisService.pushGet("push_1").getToken());
    }

    @Test
    public void 레디스_삭제(){
        //when
        String key = "key_delete";
        redisService.set(key, sample);

        //when
        redisService.delete(key);

        //then
        assertFalse(redisService.hasKey(key));
    }


    @Test(expected = IllegalArgumentException.class)
    public void IntStream_Throw(){
        // when - then
        IntStream.range(1,2).filter(i -> i!=1).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Test
    public void IntStream_FindFirst(){
        // when
        int val = IntStream.range(1,3).filter(i -> i!=1)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);

        // then
        assertThat(val, is(2));
    }
}