package com.wemakeprice.push.model;

import com.wemakeprice.push.repository.AutoPushJPARepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
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
 * Date : 2019.11.23
 */

@DataJpaTest
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
public class  AutoPushTest {
    @Autowired
    AutoPushJPARepository repository;

    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Test
    public void 로컬환경_H2_연결() throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(">>>>>> DataSource Url : "+metaData.getURL());
            assertThat(metaData.getDriverName(), is("H2 JDBC Driver"));
            assertThat(metaData.getUserName(), is("SA"));
        }
    }

    @Test
    public void JPA_H2(){
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
        assertThat(newAutoPush.getSeq(), is(1L));

        AutoPush existingAutoPush = repository.findBySeq(newAutoPush.getSeq());
        assertNotNull(existingAutoPush);
        assertThat(existingAutoPush.getSendPlanTime(), is("08:10:00"));
    }
}