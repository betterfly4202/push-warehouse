package com.wemakeprice.push.repository;

import com.wemakeprice.push.entity.AutoPush;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoPushJPARepository extends JpaRepository<AutoPush, Long> {
    AutoPush findBySeq(Long seq);
//    AutoPush findDistinctBySendPlanTime();
}
