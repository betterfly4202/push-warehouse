package com.wemakeprice.push.model;

import lombok.Builder;
import lombok.Getter;
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
@RedisHash("redisSample")
public class RedisSample implements Serializable {
    @Id
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
}
