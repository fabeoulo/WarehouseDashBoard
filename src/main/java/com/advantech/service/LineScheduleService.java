/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.service;

import com.advantech.model.Floor;
import com.advantech.model.Line;
import com.advantech.model.LineSchedule;
import com.advantech.model.LineScheduleStatus;
import com.advantech.model.LineSchedule_;
import com.advantech.model.StorageSpace;
import com.advantech.model.Warehouse;
import com.advantech.repo.LineScheduleRepository;
import static com.google.common.base.Preconditions.checkState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Wei.Cheng
 */
@Service
@Transactional
public class LineScheduleService {

    @Autowired
    private LineScheduleRepository repo;

    @Autowired
    private LineScheduleStatusService stateService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private LineService lineService;

    @Autowired
    private FloorService floorService;

    public DataTablesOutput<LineSchedule> findAll(DataTablesInput dti) {
        return repo.findAll(dti);
    }

    public DataTablesOutput<LineSchedule> findAll(DataTablesInput dti, Specification<LineSchedule> s) {
        return repo.findAll(dti, s);
    }

    public LineSchedule getOne(Integer id) {
        return repo.getOne(id);
    }

    public DataTablesOutput<LineSchedule> findSchedule(DataTablesInput input, Floor f) {
        LineScheduleStatus onboard = stateService.getOne(4);
        DateTime today = new DateTime().withTime(0, 0, 0, 0);

        return repo.findAll(input, (Root<LineSchedule> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Path<Floor> entryPath = root.get(LineSchedule_.FLOOR);
            Path<Date> datePath = root.get(LineSchedule_.CREATE_DATE);
            Path<LineScheduleStatus> statusPath = root.get(LineSchedule_.LINE_SCHEDULE_STATUS);
            return cb.and(
                    cb.equal(entryPath, f),
                    cb.notEqual(statusPath, onboard)
            );
        });
    }

    public <S extends LineSchedule> S save(S s) {
        if (isFloorChanged(s)) {
            LineScheduleStatus defaultState = stateService.getOne(1);
            s.setLine(null);
            s.setLineSchedulePriorityOrder(null);
            s.setLineScheduleStatus(defaultState);
            List<Warehouse> l = warehouseService
                    .findByPoAndFloorAndFlag(s.getPo(), s.getFloor(), 0);
            if (!l.isEmpty()) {
                Warehouse w = l.get(0);
                w.setFlag(1);
                warehouseService.save(w);
            }
        } else if (isLineChanged(s)) {
            LineScheduleStatus lineOperated = stateService.getOne(3);
            LineScheduleStatus onBoard = stateService.getOne(4);
            if (s.getLineScheduleStatus().getId() != onBoard.getId()) {
                s.setLineScheduleStatus(lineOperated);
            }
            List<Warehouse> l = warehouseService
                    .findByPoAndFloorAndFlag(s.getPo(), s.getFloor(), 0);
            if (!l.isEmpty()) {
                Warehouse w = l.get(0);
                if (w.getLineSchedule() == null) {
                    w.setLineSchedule(s);
                    warehouseService.save(w);
                }
            }

            if (s.getLineScheduleStatus().getId() != onBoard.getId()) {
                Line line = lineService.getOne(s.getLine().getId());
                List<LineSchedule> dataInLine = this.findByLine(line);
                int priorityOrder = 1;
                if (!dataInLine.isEmpty()) {
                    Integer maxPriorityOrder = dataInLine.stream().mapToInt(LineSchedule::getLineSchedulePriorityOrder).max().getAsInt();
                    priorityOrder = maxPriorityOrder + 1;
                }
                s.setLineSchedulePriorityOrder(priorityOrder);
            }
        } else if (isPriorityOrderChanged(s)) {

        }
        return repo.save(s);
    }

    private List<LineSchedule> findByLine(Line line) {
        DataTablesInput input = new DataTablesInput();
        input.setLength(-1);

        LineScheduleStatus onBoard = stateService.getOne(4);

        return repo.findAll(input, (Root<LineSchedule> root, CriteriaQuery<?> cq, CriteriaBuilder cb) -> {
            Path<Line> linePath = root.get(LineSchedule_.LINE);
            Path<LineScheduleStatus> statusPath = root.get(LineSchedule_.LINE_SCHEDULE_STATUS);
            Path<Integer> priorityPath = root.get(LineSchedule_.LINE_SCHEDULE_PRIORITY_ORDER);

            return cb.and(
                    cb.equal(linePath, line),
                    cb.notEqual(statusPath, onBoard),
                    cb.isNotNull(priorityPath)
            );
        }).getData();
    }

    private boolean isLineChanged(LineSchedule pojo) {
        LineSchedule prevPojo = repo.getOne(pojo.getId());
        Line prevLine = prevPojo.getLine();
        Line checkLine = pojo.getLine();
        Integer prevId = (prevLine == null ? null : prevLine.getId());
        Integer checkId = (checkLine == null ? null : checkLine.getId());

        return !Objects.equals(prevId, checkId);
    }

    private boolean isFloorChanged(LineSchedule pojo) {
        LineSchedule prevPojo = repo.getOne(pojo.getId());
        Floor prev = prevPojo.getFloor();
        Floor check = pojo.getFloor();
        Integer prevId = (prev == null ? null : prev.getId());
        Integer checkId = (check == null ? null : check.getId());

        return !Objects.equals(prevId, checkId);
    }

    private boolean isPriorityOrderChanged(LineSchedule pojo) {
        LineSchedule prevPojo = repo.getOne(pojo.getId());
        Integer prevPriorityOrder = prevPojo.getLineSchedulePriorityOrder();
        Integer priorityOrder = pojo.getLineSchedulePriorityOrder();
        return !Objects.equals(prevPriorityOrder, priorityOrder);
    }

    public void updateStatus(Warehouse w, LineScheduleStatus status) {
        LineScheduleStatus onBoard = stateService.getOne(4);
        Floor f = getWarehouseFloor(w);
        LineSchedule schedule = this.findFirstByPoAndFloorAndLineScheduleStatusNot(w.getPo(), f, onBoard);
        if (schedule != null) {
            checkState(!(schedule.getLine() == null && status.getId() == 4), "Can't pull out when po's line is not setting");
            schedule.setLineScheduleStatus(status);
            if (status.getId() != 4) {
                schedule.setStorageSpace(w.getStorageSpace());
            }
            repo.save(schedule);
            if (w.getLineSchedule() == null) {
                w.setLineSchedule(schedule);
                warehouseService.save(w);
            }
        }
    }

    public void batchUpdateStatus(List<Warehouse> whs, LineScheduleStatus status) {
        Floor f = getWarehouseFloor(whs.get(0));
        List<String> pos = whs.stream().map(l -> l.getPo()).collect(Collectors.toList());
        Map<String, List<Warehouse>> whMap = warehouseService.getActiveWhsByPoMap(pos, f, 0);
        LineScheduleStatus onBoard = stateService.getOne(4);
        Map<String, LineSchedule> lsMap = this.getFirstByPoMap(pos, f, onBoard);

        whs.forEach(w -> {
            LineSchedule schedule = lsMap.getOrDefault(w.getPo(), null);
            if (schedule != null) {
                List<Warehouse> samePoWhs = whMap.getOrDefault(w.getPo(), new ArrayList<>());
//                checkState(!(schedule.getLine() == null && status.getId() == 4), "Can't pull out when po's line is not setting");
                if (status.getId() != 4 || samePoWhs.isEmpty()) {
                    schedule.setLineScheduleStatus(status);
                    schedule.setStorageSpace(w.getStorageSpace());
                    repo.save(schedule);
                }

                if (w.getLineSchedule() == null) {
                    w.setLineSchedule(schedule);
                    warehouseService.save(w);
                }
            }
        });
    }

    public List<LineSchedule> findByFloorIdAndOnBoardDateGreaterThan(int floorId, Date sD) {
        return repo.findByFloorIdAndOnBoardDateGreaterThan(floorId, sD);
    }

    public LineSchedule findFirstByPoAndFloorAndLineScheduleStatusNot(String po, Floor f, LineScheduleStatus status) {
        return repo.findFirstByPoAndFloorAndLineScheduleStatusNot(po, f, status);
    }

    public List<LineSchedule> findByPosAndFloorAndLineScheduleStatusNot(List<String> pos, Floor floor, LineScheduleStatus status) {
        return repo.findByPosAndFloorAndLineScheduleStatusNot(pos, floor, status);
    }

    public Map<String, LineSchedule> getFirstByPoMap(List<String> pos, Floor floor, LineScheduleStatus status) {
        List<LineSchedule> l = repo.findByPosAndFloorAndLineScheduleStatusNot(pos, floor, status);
        return l.stream().collect(Collectors.groupingBy(
                LineSchedule::getPo,
                Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> list.isEmpty() ? null : list.get(0)
                )
        ));
    }

    private Floor getWarehouseFloor(Warehouse w) {
        StorageSpace ss = storageSpaceService.findById(w.getStorageSpace().getId()).get();
        return ss.getStorageSpaceGroup().getFloor();
    }

    public void delete(LineSchedule t) {
        repo.delete(t);
    }

}
