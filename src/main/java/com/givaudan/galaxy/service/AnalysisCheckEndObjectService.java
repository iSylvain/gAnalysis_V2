package com.givaudan.galaxy.service;

import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.enums.AnalysisCheckEndEnum;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "analysisCheckEnd")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisCheckEndObjectService {

    private String information;
    private String processCode;
    private Integer processVersion;
    private String operation;
    private AnalysisCheckEndEnum analysisCheckEndEnum;
    private String productNameStart;
    private String productCodeStart;
    private boolean checkSAP;
    private boolean checkCO;
    private boolean checkAppearance;
    private String informationAnalysis;

    @XmlElementWrapper(name = "analysisList")
    @XmlElement(name = "analysis")
    private List<Analysis> analysisList = new ArrayList<>();

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public Integer getProcessVersion() {
        return processVersion;
    }

    public void setProcessVersion(Integer processVersion) {
        this.processVersion = processVersion;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public AnalysisCheckEndEnum getAnalysisCheckEndEnum() {
        return analysisCheckEndEnum;
    }

    public void setAnalysisCheckEndEnum(AnalysisCheckEndEnum analysisCheckEndEnum) {
        this.analysisCheckEndEnum = analysisCheckEndEnum;
    }

    public String getProductNameStart() {
        return productNameStart;
    }

    public void setProductNameStart(String productNameStart) {
        this.productNameStart = productNameStart;
    }

    public String getProductCodeStart() {
        return productCodeStart;
    }

    public void setProductCodeStart(String productCodeStart) {
        this.productCodeStart = productCodeStart;
    }

    public boolean isCheckSAP() {
        return checkSAP;
    }

    public void setCheckSAP(boolean checkSAP) {
        this.checkSAP = checkSAP;
    }

    public boolean isCheckCO() {
        return checkCO;
    }

    public void setCheckCO(boolean checkCO) {
        this.checkCO = checkCO;
    }

    public boolean isCheckAppearance() {
        return checkAppearance;
    }

    public void setCheckAppearance(boolean checkAppearance) {
        this.checkAppearance = checkAppearance;
    }

    public String getInformationAnalysis() {
        return informationAnalysis;
    }

    public void setInformationAnalysis(String informationAnalysis) {
        this.informationAnalysis = informationAnalysis;
    }

    public List<Analysis> getAnalysisList() {
        return analysisList;
    }

    public void setAnalysisList(List<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

}
