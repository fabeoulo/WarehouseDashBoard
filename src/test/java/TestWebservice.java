/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.advantech.helper.WorkDateUtils;
import com.advantech.model.LineSchedule;
import com.advantech.webservice.port.PartMappingVarietyQueryPort;
import com.advantech.webservice.root.PartMappingVarietyQueryRoot;
import com.advantech.webservice.unmarshallclass.PartMappingVariety;
import com.advantech.repo.LineScheduleRepository;
import java.util.List;
import static java.util.stream.Collectors.toList;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@WebAppConfiguration
@ContextConfiguration(locations = {
    "classpath:servlet-context.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class TestWebservice {

    @Autowired
    private PartMappingVarietyQueryPort partMappingVarietyQueryPort;

    @Autowired
    private LineScheduleRepository lineScheduleRepo;

    @Autowired
    private WorkDateUtils workDateUtils;

    @Test
    @Transactional
    @Rollback(false)
    public void testPartMappingVarietyQueryPort() throws Exception {
        PartMappingVarietyQueryRoot root = new PartMappingVarietyQueryRoot();
        PartMappingVarietyQueryRoot.PARTMAPPINGVARIETY p = root.getPARTMAPPINGVARIETY();

        DateTime nextDay = workDateUtils.findNextDay();

        List<LineSchedule> l = lineScheduleRepo.findByCreateDateBetween(nextDay.toDate(), nextDay.plusDays(1).toDate());

        l.forEach(s -> {
            p.setITEMNO(s.getModelName());
            try {
                List varietys = partMappingVarietyQueryPort.query(root);
                String remark = this.combinePartMappingVarietyMessages(varietys);
                s.setRemark(remark);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });
        lineScheduleRepo.saveAll(l);
    }

    private String combinePartMappingVarietyMessages(List<PartMappingVariety> l) {
        if (l.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        List<PartMappingVariety> filterList = l.stream().filter(p -> p.getStationId() == 2 || p.getStationId() == 20).collect(toList());
        filterList.forEach(p -> {
            sb.append(p.getVarietyName());
            sb.append(": ");
            sb.append(p.getQty());
            sb.append(", ");
        });
        return sb.toString();
    }

}
