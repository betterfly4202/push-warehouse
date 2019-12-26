package com.wemakeprice.push.config;

import com.wemakeprice.push.entity.AutoPush;
import com.wemakeprice.push.item.ETLExecuteDecider;
import com.wemakeprice.push.item.PushItemReader;
import com.wemakeprice.push.item.PushItemWriter;
import com.wemakeprice.push.listener.PushJobListener;
import com.wemakeprice.push.repository.AutoPushJPARepository;
import com.wemakeprice.push.repository.AutoPushRedisRepository;
import com.wemakeprice.push.repository.ETLJPARepository;
import com.wemakeprice.push.vo.PushTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
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
    private PushTarget pushTarget;

    @Bean(name = PUSH_WAREHOUSE_JOB)
    public Job pushSegmentationJob(){
        return jobBuilderFactory.get(PUSH_WAREHOUSE_JOB)
                .incrementer(new RunIdIncrementer())
                .listener(new PushJobListener())
                .start(whichBatchTarget())
                .next(decider(null))
                    .from(decider(null))
                        .on("START")
                        .to(makeScheduleStep())
                    .from(decider(null))
                        .on("END")
                        .to(validateSegment())
                .end()
                .build();
    }

    @Bean
    public Step whichBatchTarget(){
        return stepBuilderFactory.get("whichBatchTarget")
                .tasklet(batchTargetTask(null))
                .build();
    }

    @Bean
    @StepScope
    public Tasklet batchTargetTask(
            @Value("#{jobParameters[pushTarget]}") String jobParameter){
        return (contribution, chunkContext) -> {
            this.pushTarget = PushTarget.findByTargetService(jobParameter);
            log.info(">>>> Execute batch service : {}", this.pushTarget.getTarget());

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public JobExecutionDecider decider(
            @Autowired ETLJPARepository etljpaRepository) {

        return new ETLExecuteDecider(etljpaRepository);
    }

    @Bean
    public Step makeScheduleStep(){
        return stepBuilderFactory.get("makeScheduleStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info(">>>> Make Schedule Step <<<<");

                    return RepeatStatus.FINISHED;
                })
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
