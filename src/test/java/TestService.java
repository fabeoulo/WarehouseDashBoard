/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Floor;
import com.advantech.model.LineSchedule;
import com.advantech.model.LineScheduleStatus;
import com.advantech.model.StorageSpace;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.model.Tags;
import com.advantech.model.User;
import com.advantech.model.Warehouse;
import com.advantech.service.FloorService;
import com.advantech.service.LineScheduleService;
import com.advantech.service.LineScheduleStatusService;
import com.advantech.service.StorageSpaceService;
import com.advantech.service.TagsService;
import com.advantech.service.UserService;
import com.advantech.service.WarehouseService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
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
public class TestService {

    @Autowired
    private LineScheduleService lineScheduleService;

    @Autowired
    private LineScheduleStatusService stateService;

    @Autowired
    private FloorService floorService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private UserService userService;

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private TagsService tagsService;

    @Test
    @Transactional
    @Rollback(true)
    public void testUserNotification() {
        Floor f = floorService.getOne(1);
        DataTablesInput input = new DataTablesInput();
        input.setLength(-1);
        List l = lineScheduleService.findSchedule(input, f).getData();
        assertEquals(74, l.size());

    }

    @Test
    @Transactional
    @Rollback(false)
    public void testWarehouse() {

//        List<Warehouse> datas = warehouseService.findByPoAndFloorAndFlag("TGN000740ZA", floorService.getOne(7), 0);
//        HibernateObjectPrinter.print(datas);
        //        List<Integer> ssids = Arrays.asList(153, 156);
        //        List<Warehouse> datas = warehouseService.findBySsidsAndFlag(ssids, 0);
        //        HibernateObjectPrinter.print(datas.get(3));
        //        HibernateObjectPrinter.print(datas.get(3).getLineSchedule());
        //        LineSchedule ls = datas.get(4).getLineSchedule();
        //        HibernateObjectPrinter.print(ls);
////        OK
//        List<String> pos = Arrays.asList("TGN000740ZA", "TEST3");
//        Map<String, List<Warehouse>> whmap = warehouseService.getActiveWhsByPoMap(pos, floorService.getOne(7), 0);
//        HibernateObjectPrinter.print(whmap);
//        String testS = "";
//        List<Warehouse> ll = whmap.getOrDefault(testS, new ArrayList<>());
//        if (whmap.containsKey(testS) && whmap.get(testS).size() > 1) {
//            HibernateObjectPrinter.print(whmap.get(testS).size());
//        }
//        OK
//        List<Integer> ssIds = Arrays.asList(147);
//        List<Warehouse> whs = warehouseService.findBySsidsAndFlag(ssIds, 0);
//        whs.stream().forEach(wh -> wh.setFlag(1));
//        User user = userService.findById(36).get();
//        warehouseService.batchSave(whs, user, "PULL_OUT");
    }

//    @Test
//    @Transactional
//    @Rollback(true)
    public void testTagsService() {
        List<Tags> ls = tagsService.findAll();
        List<String> names = ls.stream().map(i -> i.getName()).collect(Collectors.toList());
    }

    @Test
    @Transactional
    @Rollback(true)
    public void testStorageSpaceService() {

//        OK
//        List<StorageSpace> ss = storageSpaceService.findAllByIdOrdered(Arrays.asList(149, 151));
//        StorageSpace ss = storageSpaceService.findById(148).get();
//        HibernateObjectPrinter.print(ss);
//        StorageSpaceGroup sg = ss.getStorageSpaceGroup();
//        Floor f = sg.getFloor();
//        HibernateObjectPrinter.print(f);
//        Floor sf = floorService.findFloorByStorageSpaceId(150);
//        HibernateObjectPrinter.print(sf);
//        int sid = 150;
//        StorageSpace ls = storageSpaceService.findWithLazyById(sid);
//        HibernateObjectPrinter.print(ls);
//        Floor ff = ls.getStorageSpaceGroup().getFloor();
//        HibernateObjectPrinter.print(ff);
//        DataTablesInput input = new DataTablesInput();
//        input.setLength(-1);
//        OK
//        int ssgId = 28;
        List<Floor> f = floorService.findByIdIn(Arrays.asList(1,2,7));
//        List<StorageSpace> lss = storageSpaceService.findEmptyByFloors(f);
//        Optional<StorageSpace> ss = lss.stream().filter(l -> l.getStorageSpaceGroup().getId() == ssgId).findFirst();
//        StorageSpace ss = storageSpaceService.findFirstEmptyByFloorAndPriority(floorService.getOne(7), ssgId);
//        if (ss != null) {
//            Warehouse wh = new Warehouse("POPO809test", ss);
//            List<Warehouse> datas = Arrays.asList(wh);
//            User user = userService.findById(36).get();
//            warehouseService.batchSave(datas, user, "PUT_IN");
//        }

//        OK
        Map<String, List<StorageSpace>> m = storageSpaceService.findEmptyMapByFloors(f);
        Map<String, Integer> m2 = m.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size()
                ));
        HibernateObjectPrinter.print(m);
        List<StorageSpace> l = (List<StorageSpace>) m.values().toArray()[0];
        String florr = l.get(0).getStorageSpaceGroup().getFloor().getName();
        HibernateObjectPrinter.print(l);
    }
//    @Test
//    @Transactional
//    @Rollback(true)

    public void testLlineScheduleService() {
//        OK
        List<String> pos = Arrays.asList("TGN000740ZA", "TEST3", "");
        LineScheduleStatus onBoard = stateService.getOne(4);
        Map<String, LineSchedule> lsMap = lineScheduleService.getFirstByPoMap(pos, floorService.getOne(7), onBoard);
        LineSchedule ls = lsMap.getOrDefault("TEST3", null);
        HibernateObjectPrinter.print(lsMap);

////        OK
//        List<LineSchedule> l = lineScheduleService.findByFloorIdAndOnBoardDateGreaterThan(3, new DateTime().minusMonths(1).toDate());
//        Map<String, String> map = l.stream().collect(
//                Collectors.toMap(LineSchedule::getPo, LineSchedule::getModelName, (oldValue, newValue) -> oldValue));
    }
}
