/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.job;

import com.advantech.model.StorageSpace;
import com.advantech.model.Warehouse;
import com.advantech.service.FloorService;
import com.advantech.service.StorageSpaceService;
import com.advantech.webservice.port.WaGetTagPort;
import com.advantech.websocket.TagHandler;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class PollingTags implements PollingJob {

    private static final Logger log = LoggerFactory.getLogger(PollingTags.class);

    @Autowired
    private TagHandler socket;

    @Autowired
    private FloorService floorService;

    @Autowired
    private StorageSpaceService storageSpaceService;

    @Autowired
    private WaGetTagPort waGetTagPort;

    //抓取資料並廣播
    @Override
    public void dataBrocast() {
        try {
            socket.sendAll(getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getData() {

        List<StorageSpace> ls = storageSpaceService.findWhActiveByFloor(floorService.getOne(7));
//        List<String> names = ls.stream().map(i -> i.getTagName())
//                .filter(tagName -> tagName != null).collect(Collectors.toList());
//        Map<String, Integer> tagMap = waGetTagPort.getMapByTagnames(names);
        JSONArray jarray = new JSONArray();
        ls.forEach(ss -> {
            StringBuilder sb = new StringBuilder();
            Set<Warehouse> whs = ss.getWarehouses().stream().filter(wh -> wh.getFlag() == 0).collect(Collectors.toSet());
            whs.forEach(wh -> {
                if (wh.getFlag() == 0) {
                    sb.append(wh.getPo());
                    if (wh.getLineSchedule() != null) {
                        sb.append("/" + wh.getLineSchedule().getModelName());
                    }
                    sb.append("<br/>");
                }
            });

            int sign;
            String tagName;
            if (ss.getTagName() == null) {
                sign = -1;
                tagName = "N/A";
            } else {
                sign = ss.isBlocked() ? 1 : 0;
                tagName = ss.getTagName();
            }

            JSONObject dataObj = new JSONObject();
            dataObj.put("pos", sb);
            dataObj.put("sign", sign);
            dataObj.put("sensor", tagName);
            dataObj.put("ssName", ss.getName());
            jarray.put(dataObj);
        });
        return new Gson().toJson(jarray);
    }
}
