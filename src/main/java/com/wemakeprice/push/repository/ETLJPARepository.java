package com.wemakeprice.push.repository;

import com.wemakeprice.push.entity.ETL;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.23
 */
public interface ETLJPARepository extends JpaRepository<ETL, Long> {
    ETL findByTableName(String tableName);
}
