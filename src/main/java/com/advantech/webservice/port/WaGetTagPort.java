/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.webservice.port;

import com.advantech.model.webaccess.WaGetTagResponseModel;
import com.advantech.model.webaccess.WaGetTagResponseModel.TagNode;
import com.advantech.webservice.WebAccess;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Justin.Yeh
 */
@Component
public class WaGetTagPort extends WebAccess {

    private static final Logger log = Logger.getLogger(WaGetTagPort.class.getName());

    private String urlGetTagValue;

    private Map<String, Integer> map = new HashMap<>();

    public String getUrlGetTagValue() {
        return urlGetTagValue;
    }

    public void setUrlGetTagValue(String urlGetTagValue) {
        this.urlGetTagValue = urlGetTagValue;
    }

    public Map<String, Integer> getMap() {
        return map;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    public Map<String, Integer> getMapByTagnames(List<String> tagNames) {
        String json = getJsonString(tagNames);
        setTagToMap(getResponseBodys(json));
        return map;
    }

    private WaGetTagResponseModel getResponseBodys(String json) {
        String jsonResponse = postJson(urlGetTagValue, json);
        return jsonToObj(jsonResponse, WaGetTagResponseModel.class);
    }

    private Map<String, Integer> setTagToMap(WaGetTagResponseModel responseBodys) {
        map = responseBodys.getValues().stream()
                .filter(f -> (f.getValue() == 0 || f.getValue() == 1))
                .collect(Collectors.toMap(TagNode::getName, TagNode::getValue));

        log.log(Level.INFO, "initActiveTagNodes.map.size() : {0}", map.size());
        return map;
    }

    public String getJsonString(List<String> l) {
        JSONObject result = new JSONObject();
        JSONArray tags = new JSONArray();

        for (String s : l) {
            JSONObject tag = new JSONObject();
            tag.put("Name", s);
            tags.put(tag);
        }

        result.put("Tags", tags);
        return result.toString();
    }
}
