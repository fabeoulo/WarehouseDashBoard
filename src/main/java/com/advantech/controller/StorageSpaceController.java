/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.model.Floor;
import com.advantech.model.StorageSpace;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.model.Warehouse;
import com.advantech.service.FloorService;
import com.advantech.service.StorageSpaceService;
import com.advantech.service.WarehouseService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/StorageSpaceController")
public class StorageSpaceController {

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private FloorService floorService;

    @ResponseBody
    @RequestMapping(value = "findAll", method = {RequestMethod.GET})
    protected List<StorageSpace> findAll() {
        return storageSpaceService.findAll();
    }

    @ResponseBody
    @RequestMapping(value = "findByFloor", method = {RequestMethod.GET})
    protected List<StorageSpace> findByFloor(@ModelAttribute Floor f) {
        return storageSpaceService.findByFloor(f);
    }

    @ResponseBody
    @RequestMapping(value = "findEmptyByFloor", method = {RequestMethod.GET})
    protected List<StorageSpace> findEmptyByFloor(@ModelAttribute Floor f) {
        return storageSpaceService.findEmptyByFloor(f);
    }

    @ResponseBody
    @RequestMapping(value = "findByStorageSpaceGroup", method = {RequestMethod.GET})
    protected List<StorageSpace> findByStorageSpaceGroup(
            @Valid @ModelAttribute StorageSpaceGroup storageSpaceGroup,
            HttpServletRequest request) {
        return storageSpaceService.findByStorageSpaceGroupOrderByName(storageSpaceGroup);
    }

    @ResponseBody
    @RequestMapping(value = "findByIds", method = {RequestMethod.GET})
    protected List<StorageSpace> findByIds(
            @RequestParam List<Integer> ids,
            HttpServletRequest request) {
        return storageSpaceService.findAllById(ids);
    }

    @ResponseBody
    @RequestMapping(value = "findByPo", method = {RequestMethod.GET})
    protected List<StorageSpace> findByPo(
            @RequestParam String po,
            @RequestParam int floorId,
            HttpServletRequest request) {
        List<Warehouse> whs = warehouseService.findByPoAndFloorAndFlag(po, floorService.getOne(floorId), 0);
        List<Integer> ids = whs.stream().map(l -> l.getStorageSpace().getId()).collect(Collectors.toList());
        return storageSpaceService.findAllById(ids);
    }
}
