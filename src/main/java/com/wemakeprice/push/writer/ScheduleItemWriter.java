package com.wemakeprice.push.writer;

import com.wemakeprice.push.repository.AutoPushRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
public class ScheduleItemWriter implements ItemWriter<Object> {
    private final AutoPushRedisRepository autoPushRedisRepository;

    @Override
    public void write(List<? extends Object> items) throws Exception {
        System.out.println(items);
    }

}