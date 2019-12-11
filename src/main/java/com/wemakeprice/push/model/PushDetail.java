package com.wemakeprice.push.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter(AccessLevel.PRIVATE)
public class PushDetail implements Serializable {
    private final static String utmStringFormat = "utm_source=%d&utm_medium=%s&utm_campaign=%d&utm_content=%d";
    private final static long timestampToMills = 1_000L;

    private Integer device;
    private String title;
    private String content;
    private String image;
    private String link;
    private String utm;
    private long timestamp;
    @Setter
    private Map<String, Integer> target;

    public static PushDetail create(AutoPush autoPush){
        return PushDetail.createDetailFromAutoPush(autoPush);
    }

    private static PushDetail createDetailFromAutoPush(AutoPush autoPush){
        PushDetail pushDetail = new PushDetail();
        pushDetail.setDevice(autoPush.getDevice());
        pushDetail.setTitle(autoPush.getTitle().trim());
        pushDetail.setContent(autoPush.getContent().trim());
        pushDetail.setImage(getValueWithRemoveSpace(autoPush.getImage()));
        pushDetail.setLink(getValueWithRemoveSpace(autoPush.getEventLandingUrl()));
        pushDetail.setUtm(getUtmFromUtmCombination(autoPush));
        pushDetail.setTimestamp(getTimestampFromSendPlanTime(getSendDateTime(autoPush.getCurdate(), autoPush.getSendPlanTime())));

        return pushDetail;
    }

    protected static String getValueWithRemoveSpace(String eventLandingUrl){
        return eventLandingUrl.replaceAll(" ", "");
    }

    protected static String getUtmFromUtmCombination(AutoPush autoPush){
        return String.format("utm_source=%d&utm_medium=%s&utm_campaign=%d&utm_content=%d",
                autoPush.getUtmSource(), autoPush.getUtmMedium(), autoPush.getUtmCampaign(), autoPush.getUtmContent());
    }

    private static String getSendDateTime(String curdate, String sendPlanTime){
        return String.join(" ", curdate,sendPlanTime);
    }

    private static long getTimestampFromSendPlanTime(String sendDateTime){
        return Timestamp.valueOf(sendDateTime).getTime() / timestampToMills;
    }

    public int getPushDetailCode(){
        return (this.device + this.title + this.content + this.image + this.link + this.utm).hashCode();
    }
}
