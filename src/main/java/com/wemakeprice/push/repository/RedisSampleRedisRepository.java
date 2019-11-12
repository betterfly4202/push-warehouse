package com.wemakeprice.push.repository;

import com.wemakeprice.push.model.RedisSample;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.22
 */
public interface RedisSampleRedisRepository extends CrudRepository<RedisSample, String> {
}
