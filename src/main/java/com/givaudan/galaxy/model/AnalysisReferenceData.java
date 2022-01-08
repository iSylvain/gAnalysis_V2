package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.analysis.Analysis;
import com.givaudan.galaxy.model.core.Data;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "analysis_analysisReferenceData")
@NamedQueries({
    @NamedQuery(name = "AnalysisReferenceData.findAll", query = "select o from AnalysisReferenceData o order by o.name"),
    @NamedQuery(name = "AnalysisReferenceData.findById", query = "select o from AnalysisReferenceData o where o.id = :id"),
    @NamedQuery(name = "AnalysisReferenceData.findByName", query = "select o from AnalysisReferenceData o where o.name = :name")
})
@XmlRootElement(name = "analysisReferenceData")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisReferenceData extends Data implements Serializable {
    
    @Column(nullable = false, length = 255, unique = true)
    private String number;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysis", foreignKey = @ForeignKey(name = "fk_analysisForAnalysisReferenceData"), nullable = false)
    private Analysis analysis;

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
     * @return the analysis
     */
    public Analysis getAnalysis() {
        return analysis;
    }

    /**
     * @param analysis the analysis to set
     */
    public void setAnalysis(Analysis analysis) {
        this.analysis = analysis;
    }

}
