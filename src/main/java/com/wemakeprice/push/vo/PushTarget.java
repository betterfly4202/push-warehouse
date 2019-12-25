package com.wemakeprice.push.vo;


import lombok.Getter;

import java.util.Arrays;

@Getter
public enum PushTarget {
    INFORM("inform", "tb_autopush_interface_inform", 748, 1),
    SPREAD("spread", "tb_autopush_interface_spread", 749, 2);

    private String target;
    private String etlTableName;
    private int campaignSeq;
    private int allowTarget;

    PushTarget(String target, String etlTableName, int campaignSeq, int allowTarget) {
        this.target = target;
        this.etlTableName = etlTableName;
        this.campaignSeq = campaignSeq;
        this.allowTarget = allowTarget;
    }

    public static PushTarget findByTargetService(String jobParameter){
        return Arrays.stream(PushTarget.values())
                        .filter(pushTarget -> pushTarget.getTarget().equals(jobParameter))
                        .findAny()
                        .orElse(null);
    }
}