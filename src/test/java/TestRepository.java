/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.advantech.model.Floor;
import com.advantech.model.StorageSpace;
import com.advantech.model.User;
import com.advantech.model.UserNotification;
import com.advantech.model.Warehouse;
import com.advantech.model.WarehouseEvent;
import com.advantech.repo.FloorRepository;
import com.advantech.repo.StorageSpaceRepository;
import com.advantech.repo.UserNotificationRepository;
import com.advantech.repo.UserRepository;
import com.advantech.repo.WarehouseEventRepository;
import com.advantech.repo.WarehouseRepository;
import java.util.List;
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
        Floor f = floorRepo.findById(4).get();

        List<StorageSpace> l = storageRepo.findByFloor(f);

        assertEquals(8, l.size());

    }
    
    @Test
    @Transactional
    @Rollback(true)
    public void testWarehouse() {
        Floor f = floorRepo.findById(4).get();

        List<Warehouse> l = warehouseRepo.findByFloorAndFlag(f, 1);

        assertEquals(2, l.size());

    }

}
