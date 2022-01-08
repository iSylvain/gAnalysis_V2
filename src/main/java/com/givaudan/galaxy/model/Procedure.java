package com.givaudan.galaxy.model;

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
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "analysis_procedure")
@NamedQueries({
    @NamedQuery(name="Procedure.findAll", query="select o from Procedure o"),
    @NamedQuery(name = "Procedure.findById", query = "select o from Procedure o where o.id = :id"),
    @NamedQuery(name = "Procedure.findByProcedureCode", query = "select o from Procedure o where o.procedureCode = :procedureCode")
})
public class Procedure implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private Integer version;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
  
    @Column(nullable = false)
    @NotNull
    @Size(min = 5, max = 25)
    private String procedureCode;
    
    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(99)
    private Integer procedureVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analysisCheckEnd", foreignKey = @ForeignKey(name = "fk_analysisCheckEndForProcedure"))
    private AnalysisCheckEnd analysisCheckEnd;

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
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override 
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Procedure)) {
            return false;
        }
        Procedure other = (Procedure) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.givaudan.galaxy.model.SynthesisProcedure[ id=" + getId() + " ]";
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
     * @return the procedureCode
     */
    public String getProcedureCode() {
        return procedureCode;
    }

    /**
     * @param procedureCode the procedureCode to set
     */
    public void setProcedureCode(String procedureCode) {
        this.procedureCode = procedureCode;
    }

    /**
     * @return the procedureVersion
     */
    public Integer getProcedureVersion() {
        return procedureVersion;
    }

    /**
     * @param procedureVersion the procedureVersion to set
     */
    public void setProcedureVersion(Integer procedureVersion) {
        this.procedureVersion = procedureVersion;
    }

    /**
     * @return the analysisCheckEnd
     */
    public AnalysisCheckEnd getAnalysisCheckEnd() {
        return analysisCheckEnd;
    }

    /**
     * @param analysisCheckEnd the analysisCheckEnd to set
     */
    public void setAnalysisCheckEnd(AnalysisCheckEnd analysisCheckEnd) {
        this.analysisCheckEnd = analysisCheckEnd;
    }

}
