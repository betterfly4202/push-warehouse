package com.wemakeprice.push.common;

import com.wemakeprice.push.model.AutoPush;
import com.wemakeprice.push.repository.AutoPushJPARepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.25
 */

@Component
@RequiredArgsConstructor
public class MySQLRunner implements ApplicationRunner {
    private final AutoPushJPARepository repository;


    @Override
    public void run(ApplicationArguments args) {
        saveSample();
    }


    private void saveSample(){
        AutoPush autoPush = AutoPush.builder()
                .curdate("2019-11-27")
                .sendPlanTime("08:10:00")
                .sortNo(1)
                .mobileToken("AAA-BB-CCC")
                .device(3)
                .mId(5286549)
                .cmpnNo(1096)
                .cmpnSeq(10)
                .testGroup(1)
                .title("TEST title")
                .content("TEST content")
                .image("TEST Image")
                .eventLandingUrl("www.wemakeprice.com")
                .dealId(60000000L)
                .utmSource(11)
                .utmMedium("app_push")
                .utmCampaign(1000)
                .utmContent(10)
                .build();

        repository.save(autoPush);
    }
}
