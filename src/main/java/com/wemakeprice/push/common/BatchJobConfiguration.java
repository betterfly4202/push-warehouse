package com.wemakeprice.push.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 이충일 (betterfly@wemakeprice.com)
 * Date : 2019.11.18
 */

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BatchJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job transformPushSegmentJob(){
        return jobBuilderFactory.get("transformPushSegmentJob")
                .start(transformPushSegmentStep())
                .build();
    }

    @Bean
    public Step transformPushSegmentStep() {
        return stepBuilderFactory.get("transformPushSegmentStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>>> This is TransformPushSegmentStep <<<<<");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
