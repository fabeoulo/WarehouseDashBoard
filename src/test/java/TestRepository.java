/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.model.Floor;
import com.advantech.model.Line;
import com.advantech.model.LineSchedule;
import com.advantech.model.LineScheduleStatus;
import com.advantech.model.RemoteSchedule;
import com.advantech.model.StorageSpace;
import com.advantech.model.User;
import com.advantech.model.UserNotification;
import com.advantech.model.Warehouse;
import com.advantech.model.WarehouseEvent;
import com.advantech.repo.FloorRepository;
import com.advantech.repo.LineRepository;
import com.advantech.repo.LineScheduleRepository;
import com.advantech.repo.LineScheduleStatusRepository;
import com.advantech.repo.StorageSpaceRepository;
import com.advantech.repo.UserNotificationRepository;
import com.advantech.repo.UserRepository;
import com.advantech.repo.WarehouseEventRepository;
import com.advantech.repo.WarehouseRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;
import static org.junit.Assert.*;
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
public class TestRepository {

    @Autowired
    private FloorRepository floorRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserNotificationRepository notificationRepo;

    @Autowired
    private StorageSpaceRepository storageRepo;

    @Autowired
    private WarehouseRepository warehouseRepo;

    @Autowired
    private WarehouseEventRepository warehouseEventRepo;

    @Autowired
    private LineRepository lineRepo;

    @Autowired
    private LineScheduleRepository lineScheduleRepo;

    @Autowired
    private LineScheduleStatusRepository lineScheduleStatusRepo;

//    @Test
    @Transactional
    @Rollback(true)
    public void testUserNotification() {
        UserNotification n = notificationRepo.findById(2).get();

        List<User> l = userRepo.findByUserNotifications(n);

        assertEquals(5, l.size());

    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testModel() {
        Floor f = floorRepo.findById(1).orElse(null);
        assertNotNull(f);
        StorageSpace sp = storageRepo.findById(11).orElse(null);
        assertNotNull(sp);

        Warehouse w = new Warehouse("test", sp);
        warehouseRepo.save(w);

        User user = userRepo.findById(1).orElse(null);
        assertNotNull(user);

        for (int i = 0; i < 10; i++) {
            WarehouseEvent e = new WarehouseEvent(w, user, i % 3 == 0 ? "PUT_IN" : "PULL_OUT");
            warehouseEventRepo.save(e);
        }
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testStorageSpace() {
        StorageSpace s = storageRepo.getOne(1);

        HibernateObjectPrinter.print(s.getStorageSpaceGroup().getName());

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testWarehouse() {
        Floor f = floorRepo.findById(4).get();

        List<Warehouse> l = warehouseRepo.findByFloorAndFlag(f, 1);

        assertEquals(2, l.size());

    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testLineSchedule() {
        Line line = this.lineRepo.getOne(1);

        assertNotNull(line);

        LineSchedule sche = new LineSchedule("TEST", "TEST", 12, line);

        this.lineScheduleRepo.save(sche);

    }

//    @Test
    @Transactional
    @Rollback(false)
    public void testLineScheduleStatus() {
        LineScheduleStatus lineStatus = this.lineScheduleStatusRepo.getOne(1);

        assertNotNull(lineStatus);

        LineSchedule sche = this.lineScheduleRepo.getOne(3);

        assertNotNull(sche);

        sche.setLineScheduleStatus(lineStatus);

        this.lineScheduleRepo.save(sche);

    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testLineScheduleStatus2() {
//        DateTime sD = new DateTime().withHourOfDay(0);
//        DateTime eD = new DateTime().withHourOfDay(23);
//        LineScheduleStatus onBoard = lineScheduleStatusRepo.getOne(3);
//        LineSchedule schedule = this.lineScheduleRepo
//                .findFirstByPoAndOnBoardDateBetweenAndLineScheduleStatusNot("PCJ6112ZA", sD.toDate(), eD.toDate(), onBoard);
//        HibernateObjectPrinter.print(schedule);
    }

//    @Test
    @Transactional
    @Rollback(true)
    public void testAutoSetLineScheduleStatus() {
        List<Floor> floors = floorRepo.findAll();
        LineScheduleStatus defaultStatus = lineScheduleStatusRepo.getOne(1);
        LineScheduleStatus onboard = lineScheduleStatusRepo.getOne(4);

        DateTime tomorrow = new DateTime().plusDays(1).withTime(0, 0, 0, 0);

        List<RemoteSchedule> remoteSchedules = lineScheduleRepo.getPrepareSchedule(tomorrow.toDate());
        List<LineSchedule> lineSchedules = new ArrayList();

        remoteSchedules.forEach(s -> {
            String floorName = s.getFloorName();
            if ("M2".equals(floorName)) {
                floorName = "7F";
            } else {
                floorName = null;
            }
            final String floor = floorName;
            Floor filterFloor = floors.stream().filter(f -> f.getName().equals(floor)).findFirst().orElse(null);
            if (filterFloor != null) {
                LineSchedule sche = new LineSchedule(s.getPo(), s.getModelName(), s.getQuantity(), filterFloor, defaultStatus);
                sche.setOnBoardDate(new Date());
                lineSchedules.add(sche);
            }
        });

        //When schedule's po is already in warehouse, set status to complete.
        floors.forEach(f -> {
            List<Warehouse> warehouses = warehouseRepo.findByFloorAndFlag(f, 0);
            warehouses.forEach(w -> {
                LineSchedule s = lineSchedules.stream()
                        .filter(ls -> ls.getFloor().equals(f) && ls.getPo().equals(w.getPo()))
                        .findFirst()
                        .orElse(null);
                if (s != null) {
                    s.setLineScheduleStatus(onboard);
                }
            });
        });

        HibernateObjectPrinter.print(lineSchedules);
    }
    
//    @Test
    @Transactional
    @Rollback(true)
    public void testfindByLineScheduleStatusNotAndOnBoardDateBetween(){
//        LineScheduleStatus onboard = lineScheduleStatusRepo.getOne(4);
//        DateTime sD = new DateTime().withHourOfDay(0).withMinuteOfHour(0);
//        DateTime eD = new DateTime().withHourOfDay(23).withMinuteOfHour(59);
//
//        //Find un-finished po schedules
//        List<LineSchedule> lineSchedules = lineScheduleRepo.findByLineScheduleStatusNotAndOnBoardDateBetween(onboard, sD.toDate(), eD.toDate());
//
//        HibernateObjectPrinter.print(lineSchedules);
    }
    
    @Test
    @Transactional
    @Rollback(false)
    public void testWarehouse2(){
        Warehouse w = warehouseRepo.getOne(5);
        assertNotNull(w);
        
        LineSchedule sche = lineScheduleRepo.getOne(3);
        assertNotNull(sche);
        
        w.setLineSchedule(sche);
        warehouseRepo.save(w);
    }

}
