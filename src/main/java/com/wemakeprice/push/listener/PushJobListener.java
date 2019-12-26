package com.wemakeprice.push.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class PushJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.upgradeStatus(BatchStatus.ABANDONED);
        jobExecution.setEndTime(new Date());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
