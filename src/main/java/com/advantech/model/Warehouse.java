/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Wei.Cheng
 */
@Entity
@Table(name = "Warehouse")
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class Warehouse implements java.io.Serializable {

    private int id;
    private String po;
    private StorageSpace storageSpace;
    private int flag;
    private LineSchedule lineSchedule;

    public Warehouse() {
    }

    public Warehouse(String po, StorageSpace storageSpace) {
        this.po = po;
        this.storageSpace = storageSpace;
    }

    public Warehouse(String po, StorageSpace storageSpace, int flag) {
        this.po = po;
        this.storageSpace = storageSpace;
        this.flag = flag;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "po", length = 50)
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storageSpace_id")
    public StorageSpace getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(StorageSpace storageSpace) {
        this.storageSpace = storageSpace;
    }

    @Column(name = "flag")
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineSchedule_id")
    public LineSchedule getLineSchedule() {
        return lineSchedule;
    }

    public void setLineSchedule(LineSchedule lineSchedule) {
        this.lineSchedule = lineSchedule;
    }

}
