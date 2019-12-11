package com.wemakeprice.push.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class PushInfo implements Serializable {
    private String date;
    private String sendPlanTime;
    private Integer allowTarget;
    private Integer scheduleSeq;
    private Integer campaignSeq;
    private Integer campaignItemSeq;
    private Integer messageSeq;

}
