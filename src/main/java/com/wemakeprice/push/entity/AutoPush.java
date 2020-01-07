package com.wemakeprice.push.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoPush {
    @Id
    @GeneratedValue
    @Column(name ="push_seq", nullable = false)
    private Long seq;

    @NotEmpty @Column(nullable = false)
    private String curdate;

    @NotEmpty @Column(name ="send_plan_time", nullable = false)
    private String sendPlanTime;

    @Column(name ="sort_no", nullable = false)
    private Integer sortNo;

    @NotEmpty @Column(name ="mobile_token", nullable = false)
    private String mobileToken;

    @Column(nullable = false)
    private Integer device;

    @Column(name ="m_id", nullable = false)
    private Integer mId;

    @Column(name ="cmpn_no", nullable = false)
    private Integer cmpnNo;

    @Column(name ="cmpnSeq", nullable = false)
    private Integer cmpnSeq;

    @Column(name ="test_group", nullable = false)
    private Integer testGroup;

    @NotEmpty @Column(nullable= false)
    private String title;

    @NotEmpty @Column(nullable= false)
    private String content;

    @NotEmpty @Column(nullable= false)
    private String image;

    @NotEmpty @Column(name ="event_landing_url", nullable= false)
    private String eventLandingUrl;

    @Column(name ="deal_id",nullable= false)
    private Long dealId;

    @Column(name ="utm_source",nullable= false)
    private Integer utmSource;

    @NotEmpty @Column(name ="utm_medium",nullable= false)
    private String utmMedium;

    @Column(name ="utm_campaign",nullable= false)
    private Integer utmCampaign;

    @Column(name ="utm_content",nullable= false)
    protected Integer utmContent;

    @Builder
    public AutoPush(String curdate, String sendPlanTime, Integer sortNo, String mobileToken, Integer device, Integer mId, Integer cmpnNo, Integer cmpnSeq, Integer testGroup,
                    String title, String content, String image, String eventLandingUrl, Long dealId, Integer utmSource, String utmMedium, Integer utmCampaign, Integer utmContent){
        assertion(curdate, sendPlanTime, sortNo, mobileToken, device, mId, cmpnNo, cmpnSeq, testGroup, title, content, image, eventLandingUrl, dealId, utmSource, utmMedium, utmCampaign, utmContent);

        this.curdate = curdate;
        this.sendPlanTime = sendPlanTime;
        this.sortNo = sortNo;
        this.mobileToken = mobileToken;
        this.device = device;
        this.mId = mId;
        this.cmpnNo = cmpnNo;
        this.cmpnSeq = cmpnSeq;
        this.testGroup = testGroup;
        this.title = title;
        this.content = content;
        this.image = image;
        this.eventLandingUrl = eventLandingUrl;
        this.dealId = dealId;
        this.utmSource = utmSource;
        this.utmMedium = utmMedium;
        this.utmCampaign = utmCampaign;
        this.utmContent = utmContent;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        AutoPush autoPush = (AutoPush)o;
        return Objects.equals(seq, autoPush.seq)
                && Objects.equals(curdate, autoPush.curdate)
                && Objects.equals(sendPlanTime, autoPush.sendPlanTime)
                && Objects.equals(sortNo, autoPush.sortNo)
                && Objects.equals(mobileToken, autoPush.mobileToken)
                && Objects.equals(device, autoPush.device)
                && Objects.equals(mId, autoPush.mId)
                && Objects.equals(cmpnNo, autoPush.cmpnNo)
                && Objects.equals(cmpnSeq, autoPush.cmpnSeq)
                && Objects.equals(testGroup, autoPush.testGroup)
                && Objects.equals(title, autoPush.title)
                && Objects.equals(content, autoPush.content)
                && Objects.equals(image, autoPush.image)
                && Objects.equals(eventLandingUrl, autoPush.eventLandingUrl)
                && Objects.equals(dealId, autoPush.dealId)
                && Objects.equals(utmSource, autoPush.utmSource)
                && Objects.equals(utmMedium, autoPush.utmMedium)
                && Objects.equals(utmCampaign, autoPush.utmCampaign)
                && Objects.equals(utmContent, autoPush.utmContent)
                ;
    }

    private void assertion(String curdate, String sendPlanTime, Integer sortNo, String mobileToken, Integer device, Integer mId, Integer cmpnNo, Integer cmpnSeq, Integer testGroup,
                           String title, String content, String image, String eventLandingUrl, Long dealId, Integer utmSource, String utmMedium, Integer utmCampaign, Integer utmContent){

        Assert.hasText(curdate, "curdate must not be empty");
        Assert.hasText(sendPlanTime, "sendPlanTime must not be empty");
        Assert.hasText(String.valueOf(sortNo), "sortNo must not be empty");
        Assert.hasText(mobileToken, "mobileToken must not be empty");
        Assert.hasText(String.valueOf(device), "device must not be empty");
        Assert.hasText(String.valueOf(mId), "mId must not be empty");
        Assert.hasText(String.valueOf(cmpnNo), "cmpnNo must not be empty");
        Assert.hasText(String.valueOf(cmpnSeq), "cmpnSeq must not be empty");
        Assert.hasText(String.valueOf(testGroup), "testGroup must not be empty");
        Assert.hasText(title, "title must not be empty");
        Assert.hasText(content, "content must not be empty");
        Assert.hasText(image, "image must not be empty");
        Assert.hasText(eventLandingUrl, "eventLandingUrl must not be empty");
        Assert.hasText(String.valueOf(dealId), "dealId must not be empty");
        Assert.hasText(String.valueOf(utmSource), "utmSource must not be empty");
        Assert.hasText(utmMedium, "utmMedium must not be empty");
        Assert.hasText(String.valueOf(utmCampaign), "utmCampaign must not be empty");
        Assert.hasText(String.valueOf(utmContent), "utmContent must not be empty");
    }

}
