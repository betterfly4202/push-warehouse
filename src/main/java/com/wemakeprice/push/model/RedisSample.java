package com.wemakeprice.push.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */

@Getter
@NoArgsConstructor
@RedisHash("redisSample")
public class RedisSample implements Serializable {
    public static Gson g = new GsonBuilder().disableHtmlEscaping().create();

    @Id
    @JsonProperty("id")
    private String id;
    @Setter
    private Long point;
    private LocalDateTime refreshTime;

    @Builder
    public RedisSample(String id, Long point, LocalDateTime refreshTime){
        this.id = id;
        this.point = point;
        this.refreshTime = refreshTime;
    }

    @Override
    public String toString() {
        return g.toJson(this);
    }
}
