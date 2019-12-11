package com.wemakeprice.push.model;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */

@Getter
@NoArgsConstructor
@RedisHash("push")
public class Push implements Serializable {
    public static Gson g = new GsonBuilder().disableHtmlEscaping().create();

    @Id
    @Setter
    private Long pushId;
    @Setter
    private PushInfo pushInfo;
    private Map<Integer, PushDetail> pushDetailMap = Maps.newHashMap();

    public static Push create(@NotNull Long pushId,
                        PushInfo pushInfo,
                       PushDetail pushDetail){
        Push push = createPushInfo(pushId, pushInfo);
        push.putPushDetail(pushDetail);

        return push;
    }

    private static Push createPushInfo(Long id, PushInfo pushInfo) {
        Push push = new Push();
        push.setPushId(id);
        push.setPushInfo(pushInfo);

        return push;
    }

    public String getRedisKey(){

        return String.join("_",
                this.pushInfo.getCampaignSeq().toString(),
                this.pushInfo.getCampaignItemSeq().toString(),
                this.pushInfo.getScheduleSeq().toString());
    }

    private void putPushDetail(PushDetail detail){
        pushDetailMap.put(detail.getPushDetailCode(), detail);
    }

    @Override
    public String toString() {
        return g.toJson(this);
    }
}
