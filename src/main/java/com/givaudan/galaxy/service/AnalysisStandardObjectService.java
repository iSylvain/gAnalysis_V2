package com.givaudan.galaxy.service;

import com.givaudan.galaxy.model.AnalysisStandard;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "analysisStandard")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisStandardObjectService implements Serializable {

    @XmlElementWrapper(name = "standards")
    @XmlElement(name = "standard")
    private List<AnalysisStandard> standards = new LinkedList<>();

    public List<AnalysisStandard> getStandards() {
        return standards;
    }

    public void setStandards(List<AnalysisStandard> standards) {
        this.standards = standards;
    }
}
