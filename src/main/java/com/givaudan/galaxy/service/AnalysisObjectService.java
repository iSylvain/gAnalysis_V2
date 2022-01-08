package com.givaudan.galaxy.service;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "analysis")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisObjectService {
    
    private String number;
    private String typeOfAnalysis;
    private String summary;
    private String analysisInfo;
    private String calculation;
    private String preparation;
    private String appearanceSample;

    @XmlElementWrapper(name = "standardList")
    @XmlElement(name = "standard")
    private List<AnalysisStandardObjectService> standardList = new ArrayList<>();

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTypeOfAnalysis() {
        return typeOfAnalysis;
    }

    public void setTypeOfAnalysis(String typeOfAnalysis) {
        this.typeOfAnalysis = typeOfAnalysis;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAnalysisInfo() {
        return analysisInfo;
    }

    public void setAnalysisInfo(String analysisInfo) {
        this.analysisInfo = analysisInfo;
    }

    public String getCalculation() {
        return calculation;
    }

    public void setCalculation(String calculation) {
        this.calculation = calculation;
    }

    public String getPreparation() {
        return preparation;
    }

    public void setPreparation(String preparation) {
        this.preparation = preparation;
    }

    public String getAppearanceSample() {
        return appearanceSample;
    }

    public void setAppearanceSample(String appearanceSample) {
        this.appearanceSample = appearanceSample;
    }

    public List<AnalysisStandardObjectService> getStandardList() {
        return standardList;
    }

    public void setStandardList(List<AnalysisStandardObjectService> standardList) {
        this.standardList = standardList;
    }
}
