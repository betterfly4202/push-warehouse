package com.wemakeprice.push.item;

import com.wemakeprice.push.entity.AutoPush;
import com.wemakeprice.push.model.Push;
import org.springframework.batch.item.ItemProcessor;

public class PushItemProcessor implements ItemProcessor<AutoPush, Push>{

    @Override
    public Push process(AutoPush item) throws Exception {
        System.out.println(item);

        return new Push();
    }
}
