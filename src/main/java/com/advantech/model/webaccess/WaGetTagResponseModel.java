/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.advantech.model.webaccess;

import java.util.List;

/**
 *
 * @author Justin.Yeh
 */
public class WaGetTagResponseModel {

    private ResultInfo Result;
    
    private List<TagNode> Values;

    public ResultInfo getResult() {
        return Result;
    }

    public void setResult(ResultInfo Result) {
        this.Result = Result;
    }

    public List<TagNode> getValues() {
        return Values;
    }

    public void setValues(List<TagNode> Values) {
        this.Values = Values;
    }

    public class TagNode {

        private String Name;
        private int Value;
        private int Quality;

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getValue() {
            return Value;
        }

        public void setValue(int Value) {
            this.Value = Value;
        }

        public int getQuality() {
            return Quality;
        }

        public void setQuality(int Quality) {
            this.Quality = Quality;
        }

    }

    public class ResultInfo {

        private int Ret;

        private int Total;

        public int getRet() {
            return Ret;
        }

        public void setRet(int Ret) {
            this.Ret = Ret;
        }

        public int getTotal() {
            return Total;
        }

        public void setTotal(int Total) {
            this.Total = Total;
        }
    }
}
