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
public class ETLStatusDecider implements JobExecutionDecider {
    private final ETLJPARepository etlJpaRepository;
    private static final String PUSH_TARGET= "pushTarget";

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        final int etlStatus = etlJpaRepository.findByTableName(getBatchTarget(jobExecution).getEtlTableName()).getLastValue();

        if(etlStatus < 1){
            log.info("Job is not ready yet.");

            return new FlowExecutionStatus("END");
        }

        return new FlowExecutionStatus("START");
    }

    private PushTarget getBatchTarget(JobExecution jobExecution){
        return PushTarget.findByTargetService(jobExecution.getJobParameters().getString(PUSH_TARGET));
    }
}