package com.wemakeprice.push.model;

import com.wemakeprice.push.repository.RedisSampleRedisRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.hamcrest.core.Is.is;
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

    @After
    public void tearDown(){
        repository.deleteAll();
    }

    @Test
    public void 레디스_등록_조회(){
        //given
        String id = "betterFLY";
        LocalDateTime now = LocalDateTime.now();
        RedisSample sample = RedisSample.builder()
                .id(id)
                .refreshTime(now)
                .point(1_000L)
                .build();


        //when
        repository.save(sample);

        //then
        RedisSample loadRedis = repository.findById(id).get();
        assertThat(loadRedis.getPoint(), is(1000L));
        assertThat(loadRedis.getRefreshTime(), is(now));
    }

    @Test
    public void 레디스_등록_수정(){
        //given
        String id = "betterFLY";
        LocalDateTime now = LocalDateTime.now();
        RedisSample sample = RedisSample.builder()
                .id(id)
                .refreshTime(now)
                .point(2_000L)
                .build();


        //when
        repository.save(sample);
        RedisSample loadRedis = repository.findById(id).get();
        loadRedis.setPoint(3_0000L);
        repository.save(loadRedis);

        //then
        assertThat(loadRedis.getPoint(), is(30000L));
        assertThat(loadRedis.getRefreshTime(), is(now));
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