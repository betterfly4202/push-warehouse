package com.wemakeprice.push.reader;

import com.wemakeprice.push.entity.AutoPush;
import com.wemakeprice.push.repository.AutoPushJPARepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;


@Slf4j
@RequiredArgsConstructor
public class PushItemReader implements ItemReader<AutoPush>, StepExecutionListener {
    private final AutoPushJPARepository autoPushJPARepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    @Override
    public AutoPush read(){
        return autoPushJPARepository.findBySeq(1L);
    }
}
