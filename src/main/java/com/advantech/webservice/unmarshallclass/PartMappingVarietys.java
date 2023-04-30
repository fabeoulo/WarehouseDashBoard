/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.webservice.unmarshallclass;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Wei.Cheng
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "NewDataSet")
public class PartMappingVarietys implements Serializable, QueryResult<PartMappingVariety> {

    @XmlElement(name = "QryPartMappingVariety001", type = PartMappingVariety.class)
    private List<PartMappingVariety> QryData;

    @Override
    public List<PartMappingVariety> getQryData() {
        return QryData;
    }

    @Override
    public void setQryData(List<PartMappingVariety> QryData) {
        this.QryData = QryData;
    }

}