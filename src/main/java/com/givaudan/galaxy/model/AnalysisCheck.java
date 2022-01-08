package com.givaudan.galaxy.model;

import com.givaudan.galaxy.model.analysis.Analysis;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "analysis_analysisCheck")
@NamedQueries({
    @NamedQuery(name = "AnalysisCheck.findAllAnalysisCheck", query = "select o from AnalysisCheck o"),
    @NamedQuery(name = "AnalysisCheck.findAnalysisCheckById", query = "select o from AnalysisCheck o where o.id = :id"),
    @NamedQuery(name = "AnalysisCheck.findAnalysisListWithId", query = "select o.analysisList from AnalysisCheck o where o.id = :id")
})
@XmlRootElement(name = "analysisCheck")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnalysisCheck implements Serializable {

    @Id
    @XmlElement(name = "analysisCheckID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Lob
    private String information;

    @Transient
    public boolean informationWired() {
        return (information != null && !information.isEmpty());
    }

    private boolean checkSAP;
    private boolean checkCO;
    private boolean checkAppearance;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="id_analysisCheck", foreignKey = @ForeignKey(name = "fk_analysisListForAnalysisCheck"))
    @XmlElementWrapper(name = "analysisList")
    @XmlElement(name = "analysis")
    private Set<Analysis> analysisList = new HashSet<>();

    @Transient
    public boolean isAnalysisListWired() {
        return !analysisList.isEmpty();
    }

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
     * @return the information
     */
    public String getInformation() {
        return information;
    }

    /**
     * @param information the information to set
     */
    public void setInformation(String information) {
        this.information = information;
    }

    /**
     * @return the checkSAP
     */
    public boolean isCheckSAP() {
        return checkSAP;
    }

    /**
     * @param checkSAP the checkSAP to set
     */
    public void setCheckSAP(boolean checkSAP) {
        this.checkSAP = checkSAP;
    }

    /**
     * @return the checkCO
     */
    public boolean isCheckCO() {
        return checkCO;
    }

    /**
     * @param checkCO the checkCO to set
     */
    public void setCheckCO(boolean checkCO) {
        this.checkCO = checkCO;
    }

    /**
     * @return the checkAppearance
     */
    public boolean isCheckAppearance() {
        return checkAppearance;
    }

    /**
     * @param checkAppearance the checkAppearance to set
     */
    public void setCheckAppearance(boolean checkAppearance) {
        this.checkAppearance = checkAppearance;
    }

    /**
     * @return the analysisList
     */
    public Set<Analysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @param analysisList the analysisList to set
     */
    public void setAnalysisList(Set<Analysis> analysisList) {
        this.analysisList = analysisList;
    }

}
