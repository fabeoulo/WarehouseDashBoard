
import com.advantech.job.HandleUnfinishedSchedule;
import com.advantech.job.PollingTags;
import com.advantech.job.SyncData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestQuartzJob {

    @Autowired
    private SyncData syncData;

    @Autowired
    private HandleUnfinishedSchedule hus;

    @Autowired
    private PollingTags pollingTags;

    @Test
    @Transactional
    @Rollback(false)
    public void testPollingTags() {
        String s = pollingTags.getData();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void testSyncData() {
        syncData.execute();
    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testHandleUnfinishedSchedule() {
        hus.execute();
    }
}
