/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "QryPartMappingVariety001")
public class PartMappingVariety implements Serializable {

    @XmlElement(name = "ITEM_NO")
    private String modelName;

    @XmlElement(name = "COMMODITY_NO")
    private String commodityNo;

    @XmlElement(name = "COMMODITY_NAME")
    private String commodityName;

    @XmlElement(name = "VARIETY_NO")
    private String varietyNo;

    @XmlElement(name = "VARIETY_NAME")
    private String varietyName;

    @XmlElement(name = "RELATION_QTY")
    private int qty;

    @XmlElement(name = "STATION_ID")
    private int stationId;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCommodityNo() {
        return commodityNo;
    }

    public void setCommodityNo(String commodityNo) {
        this.commodityNo = commodityNo;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getVarietyNo() {
        return varietyNo;
    }

    public void setVarietyNo(String varietyNo) {
        this.varietyNo = varietyNo;
    }

    public String getVarietyName() {
        return varietyName;
    }

    public void setVarietyName(String varietyName) {
        this.varietyName = varietyName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

}
