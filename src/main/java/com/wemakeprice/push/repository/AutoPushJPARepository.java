package com.wemakeprice.push.repository;

import com.wemakeprice.push.model.AutoPush;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.23
 */
public interface AutoPushJPARepository extends JpaRepository<AutoPush, Long> {
    AutoPush findBySeq(Long seq);
}
