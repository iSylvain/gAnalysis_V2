package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.analysis.Analysis;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "analysis_analysisForFlow")
@NamedQueries({
    @NamedQuery(name = "AnalysisForFlow.findAllAnalysisForFlow", query = "select o from AnalysisForFlow o"),
    @NamedQuery(name = "AnalysisForFlow.findAllAnalysisForFlowWithNumber", query = "SELECT o FROM AnalysisForFlow o WHERE o.analysis.number = :number")
})
@XmlRootElement(name = "analysisForFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisForFlow implements Serializable, Comparable<AnalysisForFlow> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "analysisForFlowID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(length = 10, nullable = true)
    private String processStepNumber;

    @Transient
    public boolean processStepNumberWired() {
        return (processStepNumber != null && !processStepNumber.isEmpty());
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysis", unique = false, updatable = true, nullable = false, foreignKey = @ForeignKey(name = "fk_analysisForAnalysisForFlow"))
    @NotNull
    private Analysis analysis;

    @ManyToOne
    @JoinColumn(nullable = false, name = "id_analysisFlow", foreignKey = @ForeignKey(name = "fk_analysisFlowForAnalysisForFlow"))
    private AnalysisFlow analysisFlow;

    @Transient
    private int position;

    @PrePersist
    public void initTimeStamps() {
        if (getCreatedOn() == null) {
            setCreatedOn(new Date());
        }
        setModifiedOn(getCreatedOn());
    }

    @PreUpdate
    public void updateTimeStamp() {
        setModifiedOn(new Date());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((getAnalysis().getNumber() == null) ? 0 : getAnalysis().getNumber().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AnalysisForFlow other = (AnalysisForFlow) obj;
        if (getId() == null) {
            if (other.getId() != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(AnalysisForFlow o) {
        int position = ((AnalysisForFlow) o).getPosition();

        //ascending order
        return this.position - position;
        
        //descending order
        //return position - this.position;
    }

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
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the modifiedOn
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }

    /**
     * @param modifiedOn the modifiedOn to set
     */
    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    /**
     * @return the processStepNumber
     */
    public String getProcessStepNumber() {
        return processStepNumber;
    }

    /**
     * @param processStepNumber the processStepNumber to set
     */
    public void setProcessStepNumber(String processStepNumber) {
        this.processStepNumber = processStepNumber;
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

    /**
     * @return the analysisFlow
     */
    public AnalysisFlow getAnalysisFlow() {
        return analysisFlow;
    }

    /**
     * @param analysisFlow the analysisFlow to set
     */
    public void setAnalysisFlow(AnalysisFlow analysisFlow) {
        this.analysisFlow = analysisFlow;
    }

    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

}
