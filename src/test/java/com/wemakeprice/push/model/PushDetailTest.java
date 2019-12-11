package com.wemakeprice.push.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PushDetailTest {
    private PushDetail pushDetail;
    private AutoPush autoPush;

    @Before
    public void init(){
        //given
        autoPush = AutoPush.builder()
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
                .utmCampaign(1912031051)
                .utmContent(10)
                .build();

        pushDetail = PushDetail.create(autoPush);
    }

    @Test
    public void eventLandingUrl_공백제거(){
        //given
        String sample1 = "{\"type\":5,\"image\":\"\",\"value\":\"601670409\",\"mode\":4,\"depth\":0}";
        String sample2 = "{\"type\":5,\"image\":\"\",\"value\":\"6016   70409  \",\"mode\":4,\"depth\":0}";
        String sample3 = "{\"type\":5,\"image\":\"\",\"value\":\"   6016   704 0 9  \",\"mode\":4,\"depth\":0}";


        String sample4 = "{\"type\":17,\"value\":\"https://mw.wemakeprice.com/special/category/5000528\",\"mode\":4}";
        String sample5 = "{\"type\":17,\"value\":\" https://mw.wemakeprice.com/s pecial/category/ 5000528 \",\"mode\":4}";

        //when
        String result1 = pushDetail.getValueWithRemoveSpace(sample1);
        String result2 = pushDetail.getValueWithRemoveSpace(sample2);
        String result3 = pushDetail.getValueWithRemoveSpace(sample3);


        String result4 = pushDetail.getValueWithRemoveSpace(sample4);
        String result5 = pushDetail.getValueWithRemoveSpace(sample5);

        //then
        assertTrue(result1.equals(result2));
        assertTrue(result1.equals(result3));
        assertTrue(result2.equals(result3));
        assertTrue(result4.equals(result5));
    }

    @Test
    public void UTM_조합확인(){

        //when
        String utmTest = pushDetail.getUtmFromUtmCombination(autoPush);

        //then
        assertThat(utmTest, is("utm_source=11&utm_medium=app_push&utm_campaign=1912031051&utm_content=10"));

    }
}