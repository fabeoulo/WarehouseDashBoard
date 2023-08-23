/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Justin.Yeh
 */
public class WebApiUser {
    
    @JsonProperty("Emplr_Id")
    private String Emplr_Id;

    @JsonProperty("Local_Name")
    private String Local_Name;

    @JsonProperty("Email_Addr")
    private String Email_Addr;

    @JsonProperty("Per_Level")
    private String Per_Level;

    @JsonProperty("Dep1")
    private String Dep1;

    @JsonProperty("Dep2")
    private String Dep2;

    @JsonProperty("Dep3")
    private String Dep3;

    @JsonProperty("Cost_Center")
    private String Cost_Center;

    @JsonProperty("MgrEmail_Addr")
    private String MgrEmail_Addr;

    @JsonProperty("Active")
    private int Active;

    @JsonProperty("Shift_Id")
    private String Shift_Id;

    @JsonProperty("Dimission_Date")
    private String Dimission_Date;

    public String getEmplr_Id() {
        return Emplr_Id;
    }

    public void setEmplr_Id(String Emplr_Id) {
        this.Emplr_Id = Emplr_Id;
    }

    public String getLocal_Name() {
        return Local_Name;
    }

    public void setLocal_Name(String Local_Name) {
        this.Local_Name = Local_Name;
    }

    public String getEmail_Addr() {
        return Email_Addr;
    }

    public void setEmail_Addr(String Email_Addr) {
        this.Email_Addr = Email_Addr;
    }

    public String getPer_Level() {
        return Per_Level;
    }

    public void setPer_Level(String Per_Level) {
        this.Per_Level = Per_Level;
    }

    public String getDep1() {
        return Dep1;
    }

    public void setDep1(String Dep1) {
        this.Dep1 = Dep1;
    }

    public String getDep2() {
        return Dep2;
    }

    public void setDep2(String Dep2) {
        this.Dep2 = Dep2;
    }

    public String getDep3() {
        return Dep3;
    }

    public void setDep3(String Dep3) {
        this.Dep3 = Dep3;
    }

    public String getCost_Center() {
        return Cost_Center;
    }

    public void setCost_Center(String Cost_Center) {
        this.Cost_Center = Cost_Center;
    }

    public String getMgrEmail_Addr() {
        return MgrEmail_Addr;
    }

    public void setMgrEmail_Addr(String MgrEmail_Addr) {
        this.MgrEmail_Addr = MgrEmail_Addr;
    }

    public int getActive() {
        return Active;
    }

    public void setActive(int Active) {
        this.Active = Active;
    }

    public String getShift_Id() {
        return Shift_Id;
    }

    public void setShift_Id(String Shift_Id) {
        this.Shift_Id = Shift_Id;
    }

    public String getDimission_Date() {
        return Dimission_Date;
    }

    public void setDimission_Date(String Dimission_Date) {
        this.Dimission_Date = Dimission_Date;
    }

}
