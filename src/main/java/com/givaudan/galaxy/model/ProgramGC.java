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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "analysis_programGC")
@NamedQueries({
    @NamedQuery(name = "ProgramGC.findAllProgramGC", query = "select o from ProgramGC o order by o.name"),
    @NamedQuery(name = "ProgramGC.findProgramGCByName", query = "select o from ProgramGC o where o.name = :name")
})
@XmlRootElement(name = "program")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProgramGC implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement(name = "analysisID")
    private Long id;

    @Version
    private Integer version;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;

    @Column(length = 255, unique = true, updatable = true)
    @NotNull(message = "name_null")
    @Pattern(regexp = ".*[^\\s].*", message = "onlySpace")
    private String name;

    @Lob
    private String programInfo;

    @Column(nullable = false, precision = 5)
    @NotNull
    private int split;

    @Column(nullable = false, precision = 5, scale = 1)
    @NotNull
    private float startTime;

    @Column(nullable = false, precision = 5)
    @NotNull
    private int startTemp;

    @Column(nullable = false, precision = 5)
    @NotNull
    private int ramp;

    @Column(nullable = false, precision = 5, scale = 1)
    @NotNull
    private float finalTime;

    @Column(nullable = false, precision = 5)
    @NotNull
    private int finalTemp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_columnGC", foreignKey = @ForeignKey(name = "fk_columnGCForProgramGC"))
    @XmlTransient
    private ColumnGC column;

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
     * @return the programInfo
     */
    public String getProgramInfo() {
        return programInfo;
    }

    /**
     * @param programInfo the programInfo to set
     */
    public void setProgramInfo(String programInfo) {
        this.programInfo = programInfo;
    }

    /**
     * @return the split
     */
    public int getSplit() {
        return split;
    }

    /**
     * @param split the split to set
     */
    public void setSplit(int split) {
        this.split = split;
    }

    /**
     * @return the startTime
     */
    public float getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the startTemp
     */
    public int getStartTemp() {
        return startTemp;
    }

    /**
     * @param startTemp the startTemp to set
     */
    public void setStartTemp(int startTemp) {
        this.startTemp = startTemp;
    }

    /**
     * @return the ramp
     */
    public int getRamp() {
        return ramp;
    }

    /**
     * @param ramp the ramp to set
     */
    public void setRamp(int ramp) {
        this.ramp = ramp;
    }

    /**
     * @return the finalTime
     */
    public float getFinalTime() {
        return finalTime;
    }

    /**
     * @param finalTime the finalTime to set
     */
    public void setFinalTime(float finalTime) {
        this.finalTime = finalTime;
    }

    /**
     * @return the finalTemp
     */
    public int getFinalTemp() {
        return finalTemp;
    }

    /**
     * @param finalTemp the finalTemp to set
     */
    public void setFinalTemp(int finalTemp) {
        this.finalTemp = finalTemp;
    }

    /**
     * @return the column
     */
    public ColumnGC getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(ColumnGC column) {
        this.column = column;
    }

}
