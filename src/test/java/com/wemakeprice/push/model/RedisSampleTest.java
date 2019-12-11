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
    private PushDetail pushDetail;
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


        AutoPush autoPush = AutoPush.builder()
                    .curdate("2019-11-20")
                    .sendPlanTime("08:10:00")
                    .sortNo(1)
                    .mobileToken("AAA-BB-CCC")
                    .device(3)
                    .mId(5286549)
                    .cmpnNo(1096)
                    .cmpnSeq(10)
                    .testGroup(1)
                    .title("TEST AAA-TEST  title")
                    .content("TEST content")
                    .image("TEST Image")
                    .eventLandingUrl("www.wemakeprice.com")
                    .dealId(60000000L)
                    .utmSource(11)
                    .utmMedium("app_push")
                    .utmCampaign(1000)
                    .utmContent(10)
                .build();

        pushDetail = PushDetail.create(autoPush);
        PushInfo pushInfo = PushInfo.builder()
                .date(autoPush.getCurdate())
                .sendPlanTime(autoPush.getSendPlanTime())
                .campaignSeq(3)
                .campaignItemSeq(10)
                .scheduleSeq(999)
                .build();


        push = push.create(5555L, pushInfo, pushDetail);
    }

    @Test
    public void 레디스_레파지토리_등록_조회(){
        //when
        repository.save(sample);

        //then
        RedisSample loadRedis = repository.findById(id).get();
        assertThat(loadRedis.getPoint(), is(1000L));
    }

    @Test
    public void 레디스_레파지토리_등록_수정(){
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
        redisService.set(push.getRedisKey(), push);
    }

    @Test
    public void 레디스_Get() {
        //given
        String key = push.getRedisKey();

        //when
        Push push = redisService.get(key);

        //then
        assertThat(push.getPushId(), is(5_555L));
        assertThat(push.getPushInfo().getSendPlanTime(), is("08:10:00"));
        assertThat(push.getPushDetailMap().get(pushDetail.getPushDetailCode()).getTitle(), is("TEST AAA-TEST  title"));
    }

    @Test
    public void 레디스_삭제(){
        //when
        redisService.delete(push.getRedisKey());

        //then
        assertFalse(redisService.hasKey(push.getRedisKey()));
    }
}