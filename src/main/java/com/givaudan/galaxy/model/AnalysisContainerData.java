package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.core.Data;
import com.givaudan.galaxy.model.core.AnalysisContainer;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "analysis_analysisContainerData")
@XmlRootElement(name = "analysisContainerData")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisContainerData extends Data implements Serializable {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysisContainer", foreignKey = @ForeignKey(name = "fk_analysisContainerForAnalysisContainerData"))
    @XmlTransient
    private AnalysisContainer analysisContainer;

    /**
     * @return the analysisContainer
     */
    public AnalysisContainer getAnalysisContainer() {
        return analysisContainer;
    }

    /**
     * @param analysisContainer the analysisContainer to set
     */
    public void setAnalysisContainer(AnalysisContainer analysisContainer) {
        this.analysisContainer = analysisContainer;
    }

}
