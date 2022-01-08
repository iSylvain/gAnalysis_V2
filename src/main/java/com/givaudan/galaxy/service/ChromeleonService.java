package com.givaudan.galaxy.service;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "gc")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChromeleonService implements Serializable {
    
    private Long id;
    
    private String number;
    private String typeGC;
    
    @XmlElement(name = "comment")
    private String summary;
    
    @XmlElement(name = "program")
    private String programName;
    
    @XmlElement(name = "method")
    private String methodName;
    
    private String typeMethod;
    
    private String polarity;

    private String productName;
    
    @XmlElement(name = "name")
    private String itemName;
    
    @XmlElement(name = "material")
    private String productCode;
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the typeGC
     */
    public String getTypeGC() {
        return typeGC;
    }

    /**
     * @param typeGC the typeGC to set
     */
    public void setTypeGC(String typeGC) {
        this.typeGC = typeGC;
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary the summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return the programName
     */
    public String getProgramName() {
        return programName;
    }

    /**
     * @param programName the programName to set
     */
    public void setProgramName(String programName) {
        this.programName = programName;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @param methodName the methodName to set
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * @return the typeMethod
     */
    public String getTypeMethod() {
        return typeMethod;
    }

    /**
     * @param typeMethod the typeMethod to set
     */
    public void setTypeMethod(String typeMethod) {
        this.typeMethod = typeMethod;
    }

    /**
     * @return the polarity
     */
    public String getPolarity() {
        return polarity;
    }

    /**
     * @param polarity the polarity to set
     */
    public void setPolarity(String polarity) {
        this.polarity = polarity;
    }

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the productCode
     */
    public String getProductCode() {
        return productCode;
    }

    /**
     * @param productCode the productCode to set
     */
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
    
}
