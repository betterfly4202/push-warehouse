package com.wemakeprice.push.config;

import com.wemakeprice.push.entity.AutoPush;
import com.wemakeprice.push.item.*;
import com.wemakeprice.push.listener.PushJobListener;
import com.wemakeprice.push.reader.PushItemReader;
import com.wemakeprice.push.reader.ScheduleItemReader;
import com.wemakeprice.push.repository.AutoPushJPARepository;
import com.wemakeprice.push.repository.AutoPushRedisRepository;
import com.wemakeprice.push.repository.ETLJPARepository;
import com.wemakeprice.push.vo.PushTarget;
import com.wemakeprice.push.writer.PushItemWriter;
import com.wemakeprice.push.writer.ScheduleItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class PushJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    static final String PUSH_WAREHOUSE_JOB = "pushSegmentationJob";
    private static PushTarget pushTarget;

    @Bean(name = PUSH_WAREHOUSE_JOB)
    public Job pushSegmentationJob(){
        return jobBuilderFactory.get(PUSH_WAREHOUSE_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(new PushJobListener())
                .start(startBatchJob())
                .next(etlStatusDecider(null))
                    .from(etlStatusDecider(null))
                        .on("START")
                        .to(makeScheduleStep(null))
                    .from(etlStatusDecider(null))
                        .on("END")
                        .end()
                .end()
                .build();
    }

    @Bean
    public Step startBatchJob(){
        return stepBuilderFactory.get("startBatchJob")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> Start Batch Job <<<<");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public JobExecutionDecider etlStatusDecider(
            @Autowired ETLJPARepository etljpaRepository) {

        return new ETLStatusDecider(etljpaRepository);
    }

    @Bean
    public Step makeScheduleStep(
            @Autowired AutoPushJPARepository autoPushJPARepository) {

        return stepBuilderFactory.get("makeScheduleStep")
                .<AutoPush, AutoPush>chunk(1)
                .reader(new ScheduleItemReader(autoPushJPARepository))
                .writer(new ScheduleItemWriter(null))
                .build();
    }

    @Bean
    public Step validateSegment(){
        return stepBuilderFactory.get("validateSegment")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> ValidateSegment Step <<<<");

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step pushSegmentationStep(
            @Value("${chunkSize.write:2}") int chunkSize,
            @Autowired AutoPushJPARepository autoPushJPARepository,
            @Autowired AutoPushRedisRepository autoPushRedisRepository) {
        return stepBuilderFactory.get("pushSegmentationStep")
                .<AutoPush, AutoPush>chunk(chunkSize)
                .reader(new PushItemReader(autoPushJPARepository))
                .writer(new PushItemWriter(autoPushRedisRepository))
                .build();

    }
}
