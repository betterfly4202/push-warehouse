package com.wemakeprice.push.reader;


import com.wemakeprice.push.entity.AutoPush;
import com.wemakeprice.push.repository.AutoPushJPARepository;
import com.wemakeprice.push.vo.PushTarget;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

@Slf4j
@RequiredArgsConstructor
public class ScheduleItemReader implements ItemReader<AutoPush>, StepExecutionListener {
    private final AutoPushJPARepository autoPushJPARepository;
    private PushTarget pushTarget;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        pushTarget = PushTarget.findByTargetService(stepExecution.getJobParameters().getString("pushTarget"));
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Override
    public AutoPush read(){
        log.info("What given Item reader : {} ", pushTarget.getTarget());

        return null;
    }
}
