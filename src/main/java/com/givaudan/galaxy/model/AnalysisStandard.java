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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "analysis_analysisStandard")
@NamedQueries({
    @NamedQuery(name = "AnalysisStandard.findAllAnalysisStandard", query = "select o from AnalysisStandard o"),
    @NamedQuery(name = "AnalysisStandard.findAnalysisStandardByName", query = "select o from AnalysisStandard o where o.name = :name")
})
@XmlRootElement(name = "standard")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisStandard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "analysisStandardID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(length = 255, nullable = false)
    @NotNull(message = "name_null")
    @Pattern(regexp = ".*[^\\s].*", message = "onlySpace")
    @Size(min = 2, max = 255)
    private String name;
    
    @Column(length = 255, nullable = true)
    private String analysisStandardID;
    
    @Transient
    public boolean isAnalysisStandardIDWired() {
        return analysisStandardID != null && !analysisStandardID.isEmpty();
    }

    @Column(length = 25, nullable = true)
    private String unit;
    
    @Transient
    public boolean isUnitWired() {
        return unit != null && !unit.isEmpty();
    }

    @Column(nullable = true, precision = 10, scale = 5)
    private float minimumStandard;
    
    @Transient
    public boolean isMinimumStandardWired() {
        return minimumStandard > 0;
    }

    @Column(nullable = true, precision = 10, scale = 5)
    private float maximumStandard;
    
    @Transient
    public boolean isMaximumStandardWired() {
        return maximumStandard > 0;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysis", foreignKey = @ForeignKey(name = "fk_analysisForAnalysisStandard"))
    @XmlTransient
    private Analysis analysis;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the analysisStandardID
     */
    public String getAnalysisStandardID() {
        return analysisStandardID;
    }

    /**
     * @param analysisStandardID the analysisStandardID to set
     */
    public void setAnalysisStandardID(String analysisStandardID) {
        this.analysisStandardID = analysisStandardID;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @return the minimumStandard
     */
    public float getMinimumStandard() {
        return minimumStandard;
    }

    /**
     * @param minimumStandard the minimumStandard to set
     */
    public void setMinimumStandard(float minimumStandard) {
        this.minimumStandard = minimumStandard;
    }

    /**
     * @return the maximumStandard
     */
    public float getMaximumStandard() {
        return maximumStandard;
    }

    /**
     * @param maximumStandard the maximumStandard to set
     */
    public void setMaximumStandard(float maximumStandard) {
        this.maximumStandard = maximumStandard;
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
