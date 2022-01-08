package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.core.Data;
import com.givaudan.galaxy.model.analysis.Analysis;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "analysis_analysisData")
@NamedQueries({
    @NamedQuery(name = "AnalysisData.findAll", query = "select o from AnalysisData o order by o.name"),
    @NamedQuery(name = "AnalysisData.findById", query = "select o from AnalysisData o where o.id = :id"),
    @NamedQuery(name = "AnalysisData.findByName", query = "select o from AnalysisData o where o.name = :name")
})
@XmlRootElement(name = "analysisData")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisData extends Data implements Serializable {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysis", foreignKey = @ForeignKey(name = "fk_analysisForAnalysisData"))
    @XmlTransient
    private Analysis analysis;

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
