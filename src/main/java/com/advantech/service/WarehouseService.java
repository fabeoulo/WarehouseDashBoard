/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import com.advantech.model.LineSchedule;
import com.advantech.model.LineScheduleStatus;
import com.advantech.model.StorageSpace;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.model.User;
import com.advantech.model.Warehouse;
import com.advantech.model.WarehouseEvent;
import com.advantech.repo.WarehouseRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class WarehouseService {

    @Autowired
    private WarehouseRepository repo;

    @Autowired
    private WarehouseEventService warehouseEventService;

    @Autowired
    private LineScheduleStatusService lineScheduleStatusService;

    @Autowired
    private LineScheduleService lineScheduleService;

    @Autowired
    private StorageSpaceService storageSpaceService;

    public List<Warehouse> findAll() {
        return repo.findAll();
    }

    public Optional<Warehouse> findById(Integer id) {
        return repo.findById(id);
    }

    public List<Warehouse> findByIdsAndFlag(List<Integer> storageSpaceId, int flag) {
        List<Warehouse> l = repo.findByIdsAndFlag(storageSpaceId, flag);
        l.forEach(w -> {
            if (w.getLineSchedule() != null) {
                Hibernate.initialize(w.getLineSchedule());
                Hibernate.initialize(w.getLineSchedule().getLine());
            }
        });
        return l;
    }
    
    public List<Warehouse> findByStorageSpaceGroupAndFlag(StorageSpaceGroup storageSpaceGroup, int flag) {
        List<Warehouse> l = repo.findByStorageSpaceGroupAndFlag(storageSpaceGroup, flag);
        l.forEach(w -> {
            if (w.getLineSchedule() != null) {
                Hibernate.initialize(w.getLineSchedule());
                Hibernate.initialize(w.getLineSchedule().getLine());
            }
        });
        return l;
    }

    public List<Warehouse> findByPoAndFloorAndFlag(String po, Floor floor, int flag) {
        return repo.findByPoAndFloorAndFlag(po, floor, flag);
    }

    public <S extends Warehouse> S save(S s) {
        return repo.save(s);
    }

    public void save(Warehouse w, User user, String action) {
        repo.save(w);
        WarehouseEvent e = new WarehouseEvent(w, user, action);
        warehouseEventService.save(e);

        LineScheduleStatus status = null;
        if (null == action) {

        } else {
            switch (action) {
                case "PUT_IN":
                    status = this.lineScheduleStatusService.getOne(2);
                    break;
                case "PULL_OUT":
                    status = this.lineScheduleStatusService.getOne(4);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        this.lineScheduleService.updateStatus(w, status);
    }

    public void batchSave(List<Warehouse> whList, User user, String action) {
        repo.saveAll(whList);
        List<WarehouseEvent> whE = whList.stream().map(w -> new WarehouseEvent(w, user, action)).collect(Collectors.toList());
        warehouseEventService.saveAll(whE);
        StorageSpace ss = whList.get(0).getStorageSpace();
        ss.setBlocked(true);
        storageSpaceService.save(ss);

        LineScheduleStatus status = null;
        if (null == action) {

        } else {
            switch (action) {
                case "PUT_IN":
                    status = this.lineScheduleStatusService.getOne(2);
                    break;
                case "PULL_OUT":
                    status = this.lineScheduleStatusService.getOne(4);
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        this.lineScheduleService.batchUpdateStatus(whList, status);
    }

    public void changeStorageSpace(Warehouse w, User user) {
        repo.save(w);
        WarehouseEvent e = new WarehouseEvent(w, user, "CHANGE_AREA");
        warehouseEventService.save(e);

        if (w.getLineSchedule() != null) {
            LineSchedule schedule = lineScheduleService.getOne(w.getLineSchedule().getId());
            if (schedule != null) {
                schedule.setStorageSpace(w.getStorageSpace());
                lineScheduleService.save(schedule);
            }
        }

    }

}
