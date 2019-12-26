package com.wemakeprice.push.item;

import com.wemakeprice.push.repository.ETLJPARepository;
import com.wemakeprice.push.vo.PushTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;


@Slf4j
@RequiredArgsConstructor
public class ETLExecuteDecider implements JobExecutionDecider {
    private final PushTarget pushTarget;
    private final ETLJPARepository etlJpaRepository;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        final int etlStatus = etlJpaRepository.findByTableName(pushTarget.getEtlTableName()).getLastValue();

        if(etlStatus < 1){
            return new FlowExecutionStatus("END");
        }

        return new FlowExecutionStatus("START");
    }
}