package com.advantech.model;
// Generated 2017/4/7 下午 02:26:06 by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.voodoodyne.jackson.jsog.JSOGGenerator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Floor generated by hbm2java
 */
@Entity
@Table(name = "LineSchedule")
@JsonIdentityInfo(generator = JSOGGenerator.class)
public class LineSchedule implements java.io.Serializable {

    private int id;
    private String po;
    private String modelName;
    private int quantity;
    private Line line;
    private Floor floor;
    private StorageSpace storageSpace;
    private LineScheduleStatus lineScheduleStatus;
    private Date createDate;
    private String remark;
    private Integer lineSchedulePriorityOrder;

    @JsonIgnore
    private Set<Warehouse> warehouses = new HashSet<Warehouse>(0);

    public LineSchedule() {
    }

    public LineSchedule(String po, String modelName, int quantity, Line line) {
        this.po = po;
        this.modelName = modelName;
        this.quantity = quantity;
        this.line = line;
    }

    public LineSchedule(String po, String modelName, int quantity, Floor floor, LineScheduleStatus lineScheduleStatus) {
        this.po = po;
        this.modelName = modelName;
        this.quantity = quantity;
        this.floor = floor;
        this.lineScheduleStatus = lineScheduleStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "po", nullable = false, length = 50)
    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    @Column(name = "modelName", nullable = false, length = 50)
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(name = "quantity", nullable = false)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id")
    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storageSpace_id")
    public StorageSpace getStorageSpace() {
        return storageSpace;
    }

    public void setStorageSpace(StorageSpace storageSpace) {
        this.storageSpace = storageSpace;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    public LineScheduleStatus getLineScheduleStatus() {
        return lineScheduleStatus;
    }

    public void setLineScheduleStatus(LineScheduleStatus lineScheduleStatus) {
        this.lineScheduleStatus = lineScheduleStatus;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd'T'kk:mm:ss.SSS'Z'", timezone = "GMT+8")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", length = 23)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "lineSchedule")
    public Set<Warehouse> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Set<Warehouse> warehouses) {
        this.warehouses = warehouses;
    }

    @Column(name = "lineSchedule_priority_order")
    public Integer getLineSchedulePriorityOrder() {
        return lineSchedulePriorityOrder;
    }

    public void setLineSchedulePriorityOrder(Integer lineSchedulePriorityOrder) {
        this.lineSchedulePriorityOrder = lineSchedulePriorityOrder;
    }

}
