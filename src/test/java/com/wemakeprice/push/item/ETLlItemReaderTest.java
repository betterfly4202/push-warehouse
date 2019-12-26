package com.wemakeprice.push.item;

import com.wemakeprice.push.vo.PushTarget;
import org.junit.Before;
import org.junit.Test;

import static com.wemakeprice.push.vo.PushTarget.findByTargetService;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ETLlItemReaderTest {

    private String jobParameter;

    @Before
    public void init(){
        jobParameter = "spread";
    }

    @Test
    public void target_from_jobParameter(){
        //given
        String spread = "spread";

        //when
        PushTarget target = findByTargetService(spread);

        //then
        assertThat(target.getTarget(), is("spread"));
        assertThat(target.getEtlTableName(), is("tb_autopush_interface_spread"));
        assertThat(target.getCampaignSeq(), is(749));
    }
}