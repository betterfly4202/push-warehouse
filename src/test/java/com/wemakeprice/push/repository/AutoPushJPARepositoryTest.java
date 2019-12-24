package com.wemakeprice.push.repository;

import com.wemakeprice.push.entity.AutoPush;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.27
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Profile("dev")
public class AutoPushJPARepositoryTest {
    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Autowired
    AutoPushJPARepository repository;

    @Test
    public void 로컬환경_H2_연결() throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(metaData.getDriverName());
            System.out.println(metaData.getUserName());
        }
    }

    @Test
    public void JPA_기본데이터_만들기(){
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

        AutoPush newAutoPush = repository.save(autoPush);
        assertNotNull(newAutoPush);

        AutoPush existingAutoPush = repository.findBySeq(newAutoPush.getSeq());
        assertNotNull(existingAutoPush);
        assertThat(existingAutoPush.getSendPlanTime(), is("08:10:00"));
    }
}