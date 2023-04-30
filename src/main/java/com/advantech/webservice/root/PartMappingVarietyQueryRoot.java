//
// 此檔案是由 JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 所產生 
// 請參閱 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 一旦重新編譯來源綱要, 對此檔案所做的任何修改都將會遺失. 
// 產生時間: 2019.07.03 於 03:48:02 PM CST 
//
package com.advantech.webservice.root;

import javax.persistence.criteria.Root;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * <p>
 * anonymous complex type 的 Java 類別.
 *
 * <p>
 * 下列綱要片段會指定此類別中包含的預期內容.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="METHOD">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="PART_MAPPING_VARIETY">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="STATION_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="COMMODITY_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                   &lt;element name="VARIETY_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "method",
    "partmappingvariety",
    "extdept"
})
@XmlRootElement(name = "root")
public class PartMappingVarietyQueryRoot {

    @XmlElement(name = "METHOD", required = true)
    protected PartMappingVarietyQueryRoot.METHOD method;
    @XmlElement(name = "PART_MAPPING_VARIETY", required = true)
    protected PartMappingVarietyQueryRoot.PARTMAPPINGVARIETY partmappingvariety;
    @XmlElement(name = "EXT_DEPT", required = true)
    protected String extdept = "PD03";

    public PartMappingVarietyQueryRoot() {
        this.method = new PartMappingVarietyQueryRoot.METHOD();
        this.partmappingvariety = new PartMappingVarietyQueryRoot.PARTMAPPINGVARIETY();
    }

    /**
     * 取得 method 特性的值.
     *
     * @return possible object is {@link Root.METHOD }
     *
     */
    public PartMappingVarietyQueryRoot.METHOD getMETHOD() {
        return method;
    }

    /**
     * 設定 method 特性的值.
     *
     * @param value allowed object is {@link Root.METHOD }
     *
     */
    public void setMETHOD(PartMappingVarietyQueryRoot.METHOD value) {
        this.method = value;
    }

    /**
     * 取得 partmappingvariety 特性的值.
     *
     * @return possible object is {@link Root.PARTMAPPINGVARIETY }
     *
     */
    public PartMappingVarietyQueryRoot.PARTMAPPINGVARIETY getPARTMAPPINGVARIETY() {
        return partmappingvariety;
    }

    /**
     * 設定 partmappingvariety 特性的值.
     *
     * @param value allowed object is {@link Root.PARTMAPPINGVARIETY }
     *
     */
    public void setPARTMAPPINGVARIETY(PartMappingVarietyQueryRoot.PARTMAPPINGVARIETY value) {
        this.partmappingvariety = value;
    }

    public String getExtdept() {
        return extdept;
    }

    public void setExtdept(String extdept) {
        this.extdept = extdept;
    }

    /**
     * <p>
     * anonymous complex type 的 Java 類別.
     *
     * <p>
     * 下列綱要片段會指定此類別中包含的預期內容.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class METHOD {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "ID")
        protected String id = "Advantech.EMS.TOL.BLL.QryPartMappingVariety001";

        /**
         * 取得 value 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getValue() {
            return value;
        }

        /**
         * 設定 value 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * 取得 id 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getID() {
            return id;
        }

        /**
         * 設定 id 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setID(String value) {
            this.id = value;
        }

    }

    /**
     * <p>
     * anonymous complex type 的 Java 類別.
     *
     * <p>
     * 下列綱要片段會指定此類別中包含的預期內容.
     *
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="ITEM_NO" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="STATION_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="COMMODITY_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *         &lt;element name="VARIETY_ID" type="{http://www.w3.org/2001/XMLSchema}byte"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "itemno",
        "stationid",
        "commodityid",
        "varietyid"
    })
    public static class PARTMAPPINGVARIETY {

        @XmlElement(name = "ITEM_NO", required = true)
        protected String itemno;
        @XmlElement(name = "STATION_ID")
        protected byte stationid = -1;
        @XmlElement(name = "COMMODITY_ID")
        protected byte commodityid = -1;
        @XmlElement(name = "VARIETY_ID")
        protected byte varietyid = -1;

        /**
         * 取得 itemno 特性的值.
         *
         * @return possible object is {@link String }
         *
         */
        public String getITEMNO() {
            return itemno;
        }

        /**
         * 設定 itemno 特性的值.
         *
         * @param value allowed object is {@link String }
         *
         */
        public void setITEMNO(String value) {
            this.itemno = value;
        }

        /**
         * 取得 stationid 特性的值.
         *
         */
        public byte getSTATIONID() {
            return stationid;
        }

        /**
         * 設定 stationid 特性的值.
         *
         */
        public void setSTATIONID(byte value) {
            this.stationid = value;
        }

        /**
         * 取得 commodityid 特性的值.
         *
         */
        public byte getCOMMODITYID() {
            return commodityid;
        }

        /**
         * 設定 commodityid 特性的值.
         *
         */
        public void setCOMMODITYID(byte value) {
            this.commodityid = value;
        }

        /**
         * 取得 varietyid 特性的值.
         *
         */
        public byte getVARIETYID() {
            return varietyid;
        }

        /**
         * 設定 varietyid 特性的值.
         *
         */
        public void setVARIETYID(byte value) {
            this.varietyid = value;
        }

    }

}
