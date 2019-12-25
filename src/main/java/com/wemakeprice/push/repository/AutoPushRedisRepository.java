package com.wemakeprice.push.repository;

import com.wemakeprice.push.entity.AutoPush;
import org.springframework.data.repository.CrudRepository;

public interface AutoPushRedisRepository extends CrudRepository<AutoPush, Long> {
}
