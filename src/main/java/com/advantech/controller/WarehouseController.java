/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.controller;

import com.advantech.helper.HibernateObjectPrinter;
import com.advantech.helper.SecurityPropertiesUtils;
import com.advantech.model.StorageSpace;
import com.advantech.model.StorageSpaceGroup;
import com.advantech.model.User;
import com.advantech.model.Warehouse;
import com.advantech.service.FloorService;
import com.advantech.service.StorageSpaceGroupService;
import com.advantech.service.StorageSpaceService;
import com.advantech.service.WarehouseService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Wei.Cheng
 */
@Controller
@RequestMapping("/WarehouseController")
public class WarehouseController extends CrudController<Warehouse> {

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private StorageSpaceGroupService storageSpaceGroupService;

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private FloorService floorService;

    @ResponseBody
    @RequestMapping(value = "findAll", method = {RequestMethod.GET})
    protected List<Warehouse> findAll(HttpServletRequest request, @RequestParam int storageSpaceGroupId) throws Exception {
        StorageSpaceGroup sp = storageSpaceGroupService.getOne(storageSpaceGroupId);
        return warehouseService.findByStorageSpaceGroupAndFlag(sp, 0);
    }

    @ResponseBody
    @RequestMapping(value = "findBySsid", method = {RequestMethod.GET})
    protected List<Warehouse> findBySsid(HttpServletRequest request, @RequestParam List<Integer> ssIds) throws Exception {
        return warehouseService.findBySsidsAndFlag(ssIds, 0);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = INSERT_URL, method = {RequestMethod.POST})
    protected ResponseEntity insert(@ModelAttribute Warehouse pojo, BindingResult bindingResult) throws Exception {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        pojo.setFlag(0);
        warehouseService.save(pojo, user, "PUT_IN");
        return serverResponse(SUCCESS_MESSAGE);
    }

    @ResponseBody
    @RequestMapping(value = "batchCreate", method = {RequestMethod.POST})
    protected Map<String, Integer> batchInsert(@RequestBody Map<String, Object> dataMap, BindingResult bindingResult) throws Exception {
        List<String> pos = (List<String>) dataMap.get("pos");
        int floorId = Integer.parseInt(dataMap.get("floorId").toString());
        int ssgId = Integer.parseInt(dataMap.get("ssgId").toString());

        Map<String, Integer> ssMap = new HashMap<>();
        StorageSpace ss = storageSpaceService.findFirstEmptyByFloorAndSsg(floorService.getOne(floorId), ssgId);
        if (ss != null) {
            List<Warehouse> whs = pos.stream().map(l -> new Warehouse(l, ss, 0)).collect(Collectors.toList());
            User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
            warehouseService.batchSave(whs, user, "PUT_IN");

            ssMap.put("ssId", ss.getId());
            ssMap.put("ssgId", ss.getStorageSpaceGroup().getId());
        }
        return ssMap;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = UPDATE_URL, method = {RequestMethod.POST})
    protected ResponseEntity update(@ModelAttribute Warehouse pojo, BindingResult bindingResult) throws Exception {
        HibernateObjectPrinter.print(pojo);
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        warehouseService.save(pojo, user, "PUT_IN");
        return serverResponse(SUCCESS_MESSAGE);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = DELETE_URL, method = {RequestMethod.POST})
    protected ResponseEntity delete(@RequestParam int id) throws Exception {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        Warehouse pojo = warehouseService.findById(id).get();
        pojo.setFlag(1);
        warehouseService.save(pojo, user, "PULL_OUT");
        return serverResponse(SUCCESS_MESSAGE);
    }

    @ResponseBody
    @RequestMapping(value = "deleteFromStorageSpace", method = {RequestMethod.POST})
    protected ResponseEntity deleteFromStorageSpace(@RequestParam int ssId) throws Exception {
        List<Warehouse> whs = this.findBySsid(null, Arrays.asList(ssId));
        whs.forEach(wh -> wh.setFlag(1));
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        warehouseService.batchSave(whs, user, "PULL_OUT");
        return serverResponse(SUCCESS_MESSAGE);
    }

    @ResponseBody
    @RequestMapping(value = "changeStorageSpace", method = {RequestMethod.POST})
    protected ResponseEntity changeStorageSpace(@RequestParam int warehouseId, @RequestParam int storageSpaceId) throws Exception {
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        Warehouse w = warehouseService.findById(warehouseId).get();
        StorageSpace ss = storageSpaceService.findById(storageSpaceId).get();
        w.setStorageSpace(ss);
        warehouseService.changeStorageSpace(w, user);
        return serverResponse(SUCCESS_MESSAGE);
    }

    @ResponseBody
    @RequestMapping(value = "batchChangeStorageSpace", method = {RequestMethod.POST})
    protected ResponseEntity batchChangeStorageSpace(@RequestParam int srcSsid, @RequestParam int tarSsid) throws Exception {
        List<Warehouse> whs = this.findBySsid(null, Arrays.asList(srcSsid));
        User user = SecurityPropertiesUtils.retrieveAndCheckUserInSession();
        warehouseService.batchChangeStorageSpace(whs, user,tarSsid);
        return serverResponse(SUCCESS_MESSAGE);
    }
}
